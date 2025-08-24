package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.PaymentRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.FineResponse;
import com.mwaisaka.Library.Management.System.security.CustomUserDetails;
import com.mwaisaka.Library.Management.System.service.FineService;
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
public class FIneController {

    private final FineService fineService;

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<?> getFines(@RequestParam(required = false) Long userId,
                                      @RequestParam(required = false,defaultValue = "false") Boolean unpaidOnly,
                                      Authentication authentication) {
        try {
            if (hasAdminRole(authentication)){
                List<FineResponse> fines;
                if (userId!=null){
                    fines = unpaidOnly ?
                            fineService.getUnpaidFines(userId) :
                            fineService.getUserFines(userId);
                } else {
                    fines = fineService.getAllFines();
                }
                return ResponseEntity.ok(fines);
            } else {
                Long currentUserId = getCurrentUserId(authentication);
                List<FineResponse> fines = unpaidOnly ?
                        fineService.getUnpaidFines(currentUserId) :
                        fineService.getUserFines(currentUserId);
                return ResponseEntity.ok(fines);
            }
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<?> getFineSummary(@RequestParam(required = false) Long userId,Authentication authentication) {
        try {
            Long targetUserId;

            if (hasAdminRole(authentication) && userId!=null){
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

            return ResponseEntity.ok(summary);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/payment")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentRequest paymentRequest,
                                            Authentication authentication){
        try{
            if (!hasAdminRole(authentication) &&
            !paymentRequest.getUserId().equals(getCurrentUserId(authentication))){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You can only pay your own fines");
            }
            String result = fineService.processPayment(paymentRequest);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> generateFines(){
        try{
            fineService.generateFinesForOverdueBooks();
            return ResponseEntity.ok().body("Fines generated successfully for overdue books");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Error generating fines: " + e.getMessage());
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
