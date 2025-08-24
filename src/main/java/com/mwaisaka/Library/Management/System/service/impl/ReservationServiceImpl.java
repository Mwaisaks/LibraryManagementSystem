package com.mwaisaka.Library.Management.System.service.impl;

import com.mwaisaka.Library.Management.System.domain.dto.request.ReservationRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.ReservationResponse;
import com.mwaisaka.Library.Management.System.domain.enums.ReservationStatus;
import com.mwaisaka.Library.Management.System.domain.enums.TransactionType;
import com.mwaisaka.Library.Management.System.domain.enums.UserRole;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import com.mwaisaka.Library.Management.System.domain.models.Reservation;
import com.mwaisaka.Library.Management.System.domain.models.Transaction;
import com.mwaisaka.Library.Management.System.domain.models.User;
import com.mwaisaka.Library.Management.System.repository.*;
import com.mwaisaka.Library.Management.System.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;
    private final BookRepository bookRepository;

    public ReservationResponse reserveBook(ReservationRequest reservationRequest) {
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole() == UserRole.LIBRARIAN) {
            throw new RuntimeException("Librarians cannot reserve books");
        }
        Book book = bookRepository.findById(reservationRequest.getBookId())

                .orElseThrow(() -> new RuntimeException("Book not found"));

        if(book.getAvailableCopies() > 0){
            throw new RuntimeException("Book is currently available. Please borrow it directly instead of reserving.");
        }

        Optional<Reservation> existingReservation = reservationRepository
                .findActiveReservationByUserAndBook(user, book, ReservationStatus.ACTIVE);
        if(existingReservation.isPresent()){
            throw new RuntimeException("User already has an active reservation for this book");
        }

        Optional<Transaction> activeBorrow = transactionRepository
                .findActiveBorrowTransaction(user,book, TransactionType.BORROW);

        if(activeBorrow.isPresent()){
            throw new RuntimeException("User has already borrowed this book");
        }
         Reservation reservation = Reservation.builder()
                 .user(user)
                 .book(book)
                 .reservationDate(LocalDateTime.now())
                 .status(ReservationStatus.ACTIVE)
                 .expirationDate(LocalDateTime.now().plusDays(3))
                 .build();
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.builder()
                .reservationId(savedReservation.getId())
                .userId(savedReservation.getUser().getId())
                .bookId(savedReservation.getBook().getId())
                .bookTitle(savedReservation.getBook().getTitle())
                .reservationDate(savedReservation.getReservationDate())
                .expirationDate(savedReservation.getExpirationDate())
                .status(savedReservation.getStatus())
                .queuePosition(calculateQueuePosition(book))
                .build();

    }


    public int calculateQueuePosition(Book book) {
        return reservationRepository.countActiveReservationsForBook(book, ReservationStatus.ACTIVE);
    }

    public List<ReservationResponse> getUserReservations(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Reservation> reservations = reservationRepository.findByUserOrderByReservationDateDesc(user);

        return reservations.stream()
                .map(this::mapToReservationResponse)
                .toList();
    }

    public List<ReservationResponse> getAllReservations(){
        List<Reservation> reservations = reservationRepository.findAllOrderByReservationDateDesc();

        return reservations.stream()
                .map(this::mapToReservationResponse)
                .toList();
    }

    public String cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if(!reservation.getUser().getId().equals(userId)){
            throw new RuntimeException("You can only cancel your own reservations");
        }

        if(reservation.getStatus() != ReservationStatus.ACTIVE){
            throw new RuntimeException("Only active reservations can be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledDate(LocalDateTime.now());
        reservationRepository.save(reservation);

        return "Reservation has been cancelled";
    }

    public void processReservationWhenBookReturned(Book book) {
        Optional<Reservation> nextReservation = reservationRepository
                .findOldestActiveReservationForBook(book, ReservationStatus.ACTIVE);

        if(nextReservation.isPresent()){
            Reservation reservation = nextReservation.get();
            reservation.setStatus(ReservationStatus.AVAILABLE);
            reservation.setAvailableDate(LocalDateTime.now());

            reservation.setExpirationDate(LocalDateTime.now().plusDays(2));
            reservationRepository.save(reservation);

        }
    }

    public void expireReservations() {
        List<Reservation> expiredReservations = reservationRepository
                .findExpiredActiveReservations(LocalDateTime.now());

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);
        }
    }

    private ReservationResponse mapToReservationResponse(Reservation reservation){
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .userId(reservation.getUser().getId())
                .userName(reservation.getUser().getName())
                .bookId(reservation.getBook().getId())
                .bookTitle(reservation.getBook().getTitle())
                .reservationDate(reservation.getReservationDate())
                .expirationDate(reservation.getExpirationDate())
                .availableDate(reservation.getAvailableDate())
                .cancelledDate(reservation.getCancelledDate())
                .status(reservation.getStatus())
                .queuePosition(reservation.getStatus() == ReservationStatus.ACTIVE ?
                        calculateQueuePosition(reservation.getBook()) : null)
                .build();
    }
}
