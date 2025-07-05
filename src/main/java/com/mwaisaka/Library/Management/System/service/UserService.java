package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.request.UserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID userId);

    Page<UserResponse> getAllUsers();

    UserResponse updateUser(UUID userId, UserRequest userRequest);

    void deleteUser(UUID userId);
}