package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.request.RegisterRequest;
import com.mwaisaka.Library.Management.System.domain.dto.request.UpdateUserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse registerUser(RegisterRequest request);

    UserResponse getUserById(Long userId);

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse updateUser(Long userId, UpdateUserRequest userRequest);

    void deleteUser(Long userId);
}