package com.mwaisaka.Library.Management.System.domain.models;

import com.mwaisaka.Library.Management.System.domain.enums.FineStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "days_overdue", nullable = false)
    private Integer daysOverdue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FineStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "waiver_reason")
    private String waiverReason;

    @Column(name = "notes", length = 500)
    private String notes;

    @Table(indexes = {
            @Index(name = "idx_fine_user", columnList = "user_id"),
            @Index(name = "idx_fine_status", columnList = "status"),
            @Index(name = "idx_fine_transaction", columnList = "transaction_id"),
            @Index(name = "idx_fine_created_date", columnList = "created_date")
    })
    public static class FineIndexes {}
}
