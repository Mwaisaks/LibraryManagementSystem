package com.mwaisaka.Library.Management.System.domain.models;

import com.mwaisaka.Library.Management.System.domain.enums.FineType;
import com.mwaisaka.Library.Management.System.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "borrow_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fineAmount; //Big decimal is used for precise monetary calculations- avoid floating point rounding errors

    @Enumerated //what do I put now that this calculates the amount of fine to be paid?
    private FineType fineType;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}
