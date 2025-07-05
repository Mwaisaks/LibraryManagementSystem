package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.UserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(){

        Page<UserResponse> users = userService.getAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable UUID userId
    ){
        UserResponse user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID userId, @Valid @RequestBody UserRequest userRequest
            ){

        UserResponse updatedUser = userService.updateUser(userId, userRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable UUID userId
    ){

        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted Successfully.", HttpStatus.NO_CONTENT);
    }

}
