package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResponse;
import com.mwaisaka.Library.Management.System.domain.dto.response.LoginResponse;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.domain.dto.request.*;
import com.mwaisaka.Library.Management.System.security.CustomUserDetails;
import com.mwaisaka.Library.Management.System.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse user = userService.registerUser(request);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create session and store security context
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            // Get user details
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserResponse userResponse = new UserResponse(
                    userDetails.getUser().getId(),
                    userDetails.getUser().getName(),
                    userDetails.getUser().getEmail(),
                    userDetails.getUser().getRole()
            );

            LoginResponse loginResponse = new LoginResponse("Login successful", userResponse);
            return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            UserResponse userResponse = new UserResponse(
                    userDetails.getUser().getId(),
                    userDetails.getUser().getName(),
                    userDetails.getUser().getEmail(),
                    userDetails.getUser().getRole()
            );
            return ResponseEntity.ok(ApiResponse.success("Current user retrieved", userResponse));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("No authenticated user"));
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Password reset instructions sent to your email"));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or authentication.principal.user.id == #id")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            UserResponse updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
