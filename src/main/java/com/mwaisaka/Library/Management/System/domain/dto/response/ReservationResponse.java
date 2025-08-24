package com.mwaisaka.Library.Management.System.domain.dto.response;

import com.mwaisaka.Library.Management.System.domain.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long reservationId;
    private Long userId;
    private String userName;
    private UUID bookId;
    private String bookTitle;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;
    private LocalDateTime availableDate;
    private LocalDateTime cancelledDate;
    private ReservationStatus status;
    private Integer queuePosition;
}
