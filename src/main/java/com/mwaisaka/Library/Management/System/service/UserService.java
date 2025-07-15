package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.request.RegisterRequest;
import com.mwaisaka.Library.Management.System.domain.dto.request.UpdateUserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponse registerUser(RegisterRequest request);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
    Optional<User> findByEmail(String email);
}
