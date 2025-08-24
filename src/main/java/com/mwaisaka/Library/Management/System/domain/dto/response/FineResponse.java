package com.mwaisaka.Library.Management.System.domain.dto.response;


import com.mwaisaka.Library.Management.System.domain.enums.FineStatus;
import com.mwaisaka.Library.Management.System.domain.enums.FineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FineResponse {
    private Long fineId;
    private Long userId;
    private String userName;
    private UUID bookId;
    private String bookTitle;
    private UUID transactionId;
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
