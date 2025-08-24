package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.request.ReservationRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.ReservationResponse;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservationService {
    ReservationResponse reserveBook(ReservationRequest reservationRequest);
    List<ReservationResponse> getUserReservations(Long userId);
    List<ReservationResponse> getAllReservations();
    String cancelReservation(Long reservationId, Long userId);
    void processReservationWhenBookReturned(Book book);
    void expireReservations();
}
