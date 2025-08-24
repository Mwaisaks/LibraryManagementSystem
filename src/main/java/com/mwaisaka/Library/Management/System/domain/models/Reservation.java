package com.mwaisaka.Library.Management.System.domain.models;

import com.mwaisaka.Library.Management.System.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "available_date")
    private LocalDateTime availableDate;

    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "queue_position")
    private Integer queuePosition;

    @Column(name = "notification_sent", nullable = false)
    private Boolean notificationSent = false;

    @Table(indexes = {
            @Index(name = "idx_reservation_user_book", columnList = "user_id, book_id"),
            @Index(name = "idx_reservation_status", columnList = "status"),
            @Index(name = "idx_reservation_date", columnList = "reservation_date")
    })
    public static class ReservationIndexes {}
}
