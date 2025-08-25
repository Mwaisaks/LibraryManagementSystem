package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.ReservationRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResult;
import com.mwaisaka.Library.Management.System.domain.dto.response.ReservationResponse;
import com.mwaisaka.Library.Management.System.security.CustomUserDetails;
import com.mwaisaka.Library.Management.System.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Reservations",
        description = "Endpoints for managing reservations"
)
public class RerservationController {

    private final ReservationService reservationService;

    @Operation(
            summary = "Reserve a book",
            description = "Allows a student or teacher to reserve a book. " +
                    "Users can only reserve books for themselves."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book reserved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user tried to reserve for another user",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    public ResponseEntity<ApiResult<ReservationResponse>> reserveBook(
            @Valid @RequestBody ReservationRequest reservationRequest,
            Authentication authentication) {
        try {
            if (!hasAdminRole(authentication) &&
                    !reservationRequest.getBookId().equals(getCurrentUserId(authentication))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResult.error("You can only reserve books for yourself"));
            }

            ReservationResponse response = reservationService.reserveBook(reservationRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResult.success("Book reserved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get reservations",
            description = "Retrieve reservations. " +
                    "Admins can see reservations for all users or a specific user. " +
                    "Students/Teachers can only view their own reservations."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResult<List<ReservationResponse>>> getReservations(
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
            return ResponseEntity.ok(ApiResult.success("Reservations retrieved successfully", reservations));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Cancel a reservation",
            description = "Allows a user (student/teacher/admin) to cancel a reservation by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid reservation ID",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResult<String>> cancelReservation(
            @PathVariable Long reservationId,
            Authentication authentication) {
        try {
            Long currentUserId = getCurrentUserId(authentication);
            String result = reservationService.cancelReservation(reservationId, currentUserId);
            return ResponseEntity.ok(ApiResult.success(result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
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
