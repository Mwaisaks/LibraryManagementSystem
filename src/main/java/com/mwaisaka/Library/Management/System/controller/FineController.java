package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.PaymentRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResult;
import com.mwaisaka.Library.Management.System.domain.dto.response.FineResponse;
import com.mwaisaka.Library.Management.System.security.CustomUserDetails;
import com.mwaisaka.Library.Management.System.service.FineService;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
@Tag(
        name = "Fines",
        description = "Endpoints for managing fines"
)
public class FineController {

    private final FineService fineService;

    @Operation(
            summary = "Get fines",
            description = "Retrieve fines for the current user or for a specified user (admins only). " +
                    "Supports filtering unpaid fines."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fines retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResult<List<FineResponse>>> getFines(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, defaultValue = "false") Boolean unpaidOnly,
            Authentication authentication) {
        try {
            List<FineResponse> fines;
            if (hasAdminRole(authentication)) {
                if (userId != null) {
                    fines = unpaidOnly
                            ? fineService.getUnpaidFines(userId)
                            : fineService.getUserFines(userId);
                } else {
                    fines = fineService.getAllFines();
                }
            } else {
                Long currentUserId = getCurrentUserId(authentication);
                fines = unpaidOnly
                        ? fineService.getUnpaidFines(currentUserId)
                        : fineService.getUserFines(currentUserId);
            }
            return ResponseEntity.ok(ApiResult.success("Fines retrieved successfully", fines));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get fine summary",
            description = "Retrieve a summary of unpaid fines including total unpaid amount, count of unpaid fines, and details. " +
                    "Admins can view summaries for any user; others can only view their own."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResult<Map<String, Object>>> getFineSummary(
            @RequestParam(required = false) Long userId,
            Authentication authentication) {
        try {
            Long targetUserId;
            if (hasAdminRole(authentication) && userId != null) {
                targetUserId = userId;
            } else {
                targetUserId = getCurrentUserId(authentication);
            }

            BigDecimal totalUnpaid = fineService.getTotalUnpaidFines(targetUserId);
            List<FineResponse> unpaidFines = fineService.getUnpaidFines(targetUserId);

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalUnpaidAmount", totalUnpaid);
            summary.put("unpaidFinesCount", unpaidFines.size());
            summary.put("unpaidFines", unpaidFines);

            return ResponseEntity.ok(ApiResult.success("Fine summary retrieved successfully", summary));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
    }


    @Operation(
            summary = "Process fine payment",
            description = "Allows users to pay their fines. Admins can pay fines on behalf of other users. " +
                    "Non-admins can only pay their own fines."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment request",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (trying to pay another user's fine without admin role)",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping("/payment")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResult<String>> processPayment(
            @Valid @RequestBody PaymentRequest paymentRequest,
            Authentication authentication) {
        try {
            if (!hasAdminRole(authentication) &&
                    !paymentRequest.getUserId().equals(getCurrentUserId(authentication))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResult.error("You can only pay your own fines"));
            }
            String result = fineService.processPayment(paymentRequest);
            return ResponseEntity.ok(ApiResult.success(result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Generate fines",
            description = "Generate fines for all overdue books. Restricted to librarians only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fines generated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error while generating fines",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping("/generate")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResult<String>> generateFines() {
        try {
            fineService.generateFinesForOverdueBooks();
            return ResponseEntity.ok(ApiResult.success("Fines generated successfully for overdue books"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResult.error("Error generating fines: " + e.getMessage()));
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
