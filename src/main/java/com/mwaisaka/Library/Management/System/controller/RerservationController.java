package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.ReservationRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResponse;
import com.mwaisaka.Library.Management.System.domain.dto.response.ReservationResponse;
import com.mwaisaka.Library.Management.System.security.CustomUserDetails;
import com.mwaisaka.Library.Management.System.security.CustomUserDetailsService;
import com.mwaisaka.Library.Management.System.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/reservations")
@RequiredArgsConstructor
public class RerservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    public ResponseEntity<ApiResponse<ReservationResponse>> reserveBook(
            @Valid @RequestBody ReservationRequest reservationRequest,
            Authentication authentication) {
        try {
            if (!hasAdminRole(authentication) &&
                    !reservationRequest.getBookId().equals(getCurrentUserId(authentication))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("You can only reserve books for yourself"));
            }

            ReservationResponse response = reservationService.reserveBook(reservationRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Book reserved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservations(
            @RequestParam(required = false) Long userId,
            Authentication authentication) {
        try {
            List<ReservationResponse> reservations;
            if (hasAdminRole(authentication)) {
                if (userId != null) {
                    reservations = reservationService.getUserReservations(userId);
                } else {
                    reservations = reservationService.getAllReservations();
                }
            } else {
                Long currentUserId = getCurrentUserId(authentication);
                reservations = reservationService.getUserReservations(currentUserId);
            }
            return ResponseEntity.ok(ApiResponse.success("Reservations retrieved successfully", reservations));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId,Authentication authentication){
        try {
            Long currentUserId = getCurrentUserId(authentication);
            String result = reservationService.cancelReservation(reservationId,currentUserId);
            return ResponseEntity.ok().body(result);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean hasAdminRole(Authentication authentication){
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    private Long getCurrentUserId(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }
}
