package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.UpdateUserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResult;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
@Tag(
        name = "Users",
        description = "Endpoints for managing User of  the library"
)
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Retrieve a paginated list of all users in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResult<Page<UserResponse>>> getUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(
                ApiResult.success("Users retrieved successfully", users)
        );
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a single user by their unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResult<UserResponse>> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResult.success("User retrieved successfully", user));
    }

    @Operation(
            summary = "Update user",
            description = "Update the details of an existing user by their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResult<UserResponse>> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse updatedUser = userService.updateUser(userId, updateUserRequest);
        return ResponseEntity.ok(ApiResult.success("User updated successfully", updatedUser));
    }

    @Operation(
            summary = "Delete user",
            description = "Delete a user from the system by their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResult<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResult.success("User deleted successfully",null)
        );
    }
}
