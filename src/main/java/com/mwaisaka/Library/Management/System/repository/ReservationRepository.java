package com.mwaisaka.Library.Management.System.repository;

import com.mwaisaka.Library.Management.System.domain.enums.ReservationStatus;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import com.mwaisaka.Library.Management.System.domain.models.Reservation;
import com.mwaisaka.Library.Management.System.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.user = :user AND r.book = :book AND r.status = :status")
    Optional<Reservation> findActiveReservationByUserAndBook(
            @Param("user") User user,
            @Param("book") Book book,
            @Param("status")ReservationStatus status
            );

    List<Reservation> findByUserOrderByReservationDateDesc(User user);

    @Query("SELECT r FROM Reservation r ORDER BY r.reservationDate DESC")
    List<Reservation>  findAllOrderByReservationDateDesc();

    @Query("SELECT r FROM Reservation r WHERE r.book = :book AND r.status = :status ORDER BY r.reservationDate ASC")
    Optional<Reservation> findOldestActiveReservationForBook(
            @Param("book") Book book,
            @Param("status") ReservationStatus status
    );

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.book = :book AND r.status = :status")
    int countActiveReservationsForBook(
            @Param("book") Book book,
            @Param("status") ReservationStatus status
    );

    @Query("SELECT r FROM Reservation r WHERE r.expirationDate < :currentTime AND r.status IN ('ACTIVE', 'AVAILABLE')")
    List<Reservation> findExpiredActiveReservations(@Param("currentTime")LocalDateTime currentTime);

    @Query("SELECT r FROM Reservation r WHERE r.user = :user AND r.status = :status ORDER BY r.reservationDate ASC")
    List<Reservation> findByUserAndStatusOrderByReservationDateAsc(
            @Param("user") User user,
            @Param("status")  ReservationStatus status
    );

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user = :user AND r.status = 'ACTIVE'")
    int countActiveReservationsByUser(@Param("user") User user);
}
