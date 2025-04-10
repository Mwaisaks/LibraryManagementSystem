package com.mwaisaka.Library.Management.System.Dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BorrowResponse {

    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private BigDecimal fineAmount;
    private String status;
}
