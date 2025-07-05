package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.service.impl.UserServiceImpl;
import com.mwaisaka.Library.Management.System.domain.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(){

        Page<UserDto> users = userService.getAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);

    }

}
