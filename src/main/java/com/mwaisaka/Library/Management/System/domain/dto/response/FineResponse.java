package com.mwaisaka.Library.Management.System.domain.dto.response;


import com.mwaisaka.Library.Management.System.domain.enums.FineStatus;
import com.mwaisaka.Library.Management.System.domain.enums.FineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FineResponse {
    private Long fineId;
    private Long userId;
    private String userName;
    private Integer bookId;
    private String bookTitle;
    private Long transactionId;
    private BigDecimal amount;
    private BigDecimal amountPaid;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private LocalDateTime paidDate;
    private LocalDateTime returnDate;
    private Integer daysOverdue;
    private FineStatus status;
    private String reason;
}
