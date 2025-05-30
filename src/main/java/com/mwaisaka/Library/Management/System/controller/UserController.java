package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.Dto.request.UpdateUserRequest;
import com.mwaisaka.Library.Management.System.Dto.response.MessageResponse;
import com.mwaisaka.Library.Management.System.Dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.models.User;
import com.mwaisaka.Library.Management.System.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateUserRequest request,
                                                      Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateUser(id, request, currentUser.getEmail(), currentUser.getRole()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}