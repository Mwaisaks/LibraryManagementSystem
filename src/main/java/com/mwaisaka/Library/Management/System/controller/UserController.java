package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserServiceImpl userService;


    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getUsers(){
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
