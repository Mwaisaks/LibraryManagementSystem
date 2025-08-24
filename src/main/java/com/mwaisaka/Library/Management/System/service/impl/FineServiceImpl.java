package com.mwaisaka.Library.Management.System.service.impl;

import com.mwaisaka.Library.Management.System.domain.dto.request.PaymentRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.FineResponse;
import com.mwaisaka.Library.Management.System.domain.enums.FineStatus;
import com.mwaisaka.Library.Management.System.domain.enums.TransactionType;
import com.mwaisaka.Library.Management.System.domain.models.Fine;
import com.mwaisaka.Library.Management.System.domain.models.Transaction;
import com.mwaisaka.Library.Management.System.domain.models.User;
import com.mwaisaka.Library.Management.System.repository.FineRepository;
import com.mwaisaka.Library.Management.System.repository.TransactionRepository;
import com.mwaisaka.Library.Management.System.repository.UserRepository;
import com.mwaisaka.Library.Management.System.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    public final FineRepository fineRepository;
    public final UserRepository userRepository;
    public final TransactionRepository transactionRepository;

    @Value("${library.fine.daily-rate:1.00}")
    private BigDecimal dailyFineRate;

    @Value("${library.fine.max-fine:50.00}")
    private BigDecimal maxFineAmount;

    @Override
    public List<FineResponse> getUserFines(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Fine> fines = fineRepository.findByUserOrderByCreatedDateDesc(user);

        return fines.stream()
                .map(this::mapToFineResponse)
                .toList();
    }

    @Override
    public List<FineResponse> getAllFines() {
        List<Fine> fines = fineRepository.findAllOrderByCreatedDateDesc();

        return fines.stream()
                .map(this::mapToFineResponse)
                .toList();
    }

    @Override
    public List<FineResponse> getUnpaidFines(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Fine> unpaidFines = fineRepository.findByUserAndStatus(user, FineStatus.UNPAID);

        return unpaidFines.stream()
                .map(this::mapToFineResponse)
                .toList();
    }

    @Override
    public BigDecimal getTotalUnpaidFines(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return fineRepository.getTotalUnpaidFinesByUser(user)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public String processPayment(PaymentRequest paymentRequest) {
        User user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (paymentRequest.getFineId() != null){
            return paySpecificFine(paymentRequest.getFineId(),paymentRequest.getAmount());
        } else {
            return payUserFines(user,paymentRequest.getAmount());
        }
    }

    @Override
    public void generateFinesForOverdueBooks() {
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> overdueTransactions = transactionRepository
                .findOverDueBorrowTransaction(now, TransactionType.BORROW);

        for (Transaction transaction : overdueTransactions) {
            Optional<Fine> existingFine = fineRepository
                    .findByTransaction(transaction);

            if (existingFine.isEmpty()){
                BigDecimal fineAmount = calculateFineAmount(transaction.getDueDate(),now);

                Fine fine = Fine.builder()
                        .user(transaction.getUser())
                        .book(transaction.getBook())
                        .transaction(transaction)
                        .amount(fineAmount)
                        .createdDate(now)
                        .dueDate(transaction.getDueDate())
                        .daysOverdue(calculateDaysOverdue(transaction.getDueDate(),now))
                        .status(FineStatus.UNPAID)
                        .reason("Overdue book return")
                        .build();
                fineRepository.save(fine);
            } else {
                Fine fine = existingFine.get();
                if (fine.getStatus() == FineStatus.UNPAID) {
                    BigDecimal updatedAmount = calculateFineAmount(transaction.getDueDate(),now);
                    fine.setAmount(updatedAmount);
                    fine.setDaysOverdue(calculateDaysOverdue(transaction.getDueDate(),now));
                    fineRepository.save(fine);
                }
            }
        }
    }

    @Override
    public void updateFineStatusOnReturn(Transaction returnTransaction) {
        Optional<Fine> fine = fineRepository.findByTransaction(returnTransaction);

        if(fine.isEmpty()){
            Fine existingFine = fine.get();

            existingFine.setReturnDate(returnTransaction.getReturnDate());

            fineRepository.save(existingFine);
        }
    }

    private String paySpecificFine(Long fineId, BigDecimal amount) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Fine not found"));

        if(fine.getStatus() == FineStatus.PAID) {
            throw new RuntimeException("Fine has already been paid");
        }

        if (amount.compareTo(fine.getAmount()) < 0) {
            throw new RuntimeException("Payment amount is less than the fine amount");
        }

        fine.setStatus(FineStatus.PAID);
        fine.setPaidDate(LocalDateTime.now());
        fine.setAmountPaid(fine.getAmount());
        fineRepository.save(fine);

        BigDecimal change = amount.subtract(fine.getAmount());
        String message = "Fine paid successfully. Amount: $" + fine.getAmount();

        if (change.compareTo(BigDecimal.ZERO) < 0) {
            message += ". Change: $" + change;
        }

        return message;
    }

    private String payUserFines(User user, BigDecimal paymentAmount) {
        List<Fine> unpaidFines = fineRepository.findByUserAndStatusOrderByCreatedDateAsc(user,FineStatus.UNPAID);

        if (unpaidFines.isEmpty()) {
            throw new RuntimeException("No unpaid fines found for this user");
        }

        BigDecimal remainingAmount = paymentAmount;
        int finesPaid = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;

        for (Fine fine : unpaidFines) {
            if (remainingAmount.compareTo(fine.getAmount()) >= 0) {
                fine.setStatus(FineStatus.PAID);
                fine.setPaidDate(LocalDateTime.now());
                fine.setAmountPaid(fine.getAmount());
                remainingAmount = remainingAmount.subtract(fine.getAmount());
                totalPaid = totalPaid.add(fine.getAmount());
                finesPaid++;
                fineRepository.save(fine);
            } else if (remainingAmount.compareTo(BigDecimal.ZERO) > 0){
                fine.setAmountPaid(fine.getAmountPaid() != null ?
                        fine.getAmountPaid().add(remainingAmount) : remainingAmount);
                fine.setAmount(fine.getAmount().subtract(remainingAmount));
                totalPaid = totalPaid.add(fine.getAmount());
                remainingAmount = BigDecimal.ZERO;
                fineRepository.save(fine);
                break;
            } else {
                break;
            }
        }
        String message = String.format("Payment processed successfully. %d fine(s) paid. Total amount paid: $%.2f",
                finesPaid, totalPaid);
        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0){
            message += String.format(". Change: $%.2f", remainingAmount);
        }
        return message;
    }

    private BigDecimal calculateFineAmount(LocalDateTime dueDate, LocalDateTime currentDate) {
        long daysOverDue = ChronoUnit.DAYS.between(dueDate,currentDate);

        if (daysOverDue <= 0){
            return BigDecimal.ZERO;
        }

        BigDecimal fineAmount = dailyFineRate.multiply(BigDecimal.valueOf(daysOverDue));

        return fineAmount.min(maxFineAmount);
    }

    private int calculateDaysOverdue(LocalDateTime duedate,LocalDateTime currentDate){
        long days = ChronoUnit.DAYS.between(duedate,currentDate);
        return Math.max(0,(int) days);
    }

    private FineResponse mapToFineResponse(Fine fine){
        return FineResponse.builder()
                .fineId(fine.getId())
                .userId(fine.getUser().getId())
                .userName(fine.getUser().getName())
                .bookId(fine.getBook().getId())
                .bookTitle(fine.getBook().getTitle())
                .transactionId(fine.getTransaction().getId())
                .amount(fine.getAmount())
                .amountPaid(fine.getAmountPaid())
                .createdDate(fine.getCreatedDate())
                .dueDate(fine.getDueDate())
                .paidDate(fine.getPaidDate())
                .returnDate(fine.getReturnDate())
                .daysOverdue(fine.getDaysOverdue())
                .status(fine.getStatus())
                .reason(fine.getReason())
                .build();
    }
}
