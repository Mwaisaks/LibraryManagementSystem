package com.mwaisaka.Library.Management.System.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class BorrowResponse {

    private UUID id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private BigDecimal fineAmount;
    private String status;
}
