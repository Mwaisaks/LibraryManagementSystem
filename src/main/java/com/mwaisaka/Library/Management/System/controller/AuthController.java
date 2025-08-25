package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResult;
import com.mwaisaka.Library.Management.System.domain.dto.response.LoginResponse;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.domain.dto.request.*;
import com.mwaisaka.Library.Management.System.security.CustomUserDetails;
import com.mwaisaka.Library.Management.System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Authentication",
        description = "Endpoints for user authentication and management"
)
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "User registered successfully",
            content = @Content(schema=@Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400",description = "Invalid request or regitration failed",
            content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResult<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse user = userService.registerUser(request);
            return ResponseEntity.ok(ApiResult.success("User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResult.error("Registration failed: " + e.getMessage()));
        }
    }

    @Operation(summary = "Login a user and start a session")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResult<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserResponse userResponse = new UserResponse(
                    userDetails.getUser().getId(),
                    userDetails.getUser().getName(),
                    userDetails.getUser().getEmail(),
                    userDetails.getUser().getRole()
            );

            LoginResponse loginResponse = new LoginResponse("Login successful", userResponse);
            return ResponseEntity.ok(ApiResult.success("Login successful", loginResponse));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResult.error("Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResult.error("Login failed: " + e.getMessage()));
        }
    }

    @Operation(summary = "Logout the current user and Invalidate session")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Logged out successfully",
            content = @Content(schema=@Schema(implementation = ApiResult.class))),
    })
    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResult<String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResult.success("Logged out successfully"));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<ApiResult<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            UserResponse userResponse = new UserResponse(
                    userDetails.getUser().getId(),
                    userDetails.getUser().getName(),
                    userDetails.getUser().getEmail(),
                    userDetails.getUser().getRole()
            );
            return ResponseEntity.ok(ApiResult.success("Current user retrieved", userResponse));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResult.error("No authenticated user"));
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<ApiResult<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(ApiResult.success("Password reset instructions sent to your email"));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<ApiResult<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(ApiResult.success("Password reset successfully"));
    }
    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or authentication.principal.user.id == #id")
    public ResponseEntity<ApiResult<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            UserResponse updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResult.success("User updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResult.error(e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResult<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResult.success("User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResult.error(e.getMessage()));
        }
    }
}
