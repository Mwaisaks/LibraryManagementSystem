package com.mwaisaka.Library.Management.System.service.impl;

import com.mwaisaka.Library.Management.System.domain.dto.request.UserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.domain.models.User;
import com.mwaisaka.Library.Management.System.exceptions.UserNotFoundException;
import com.mwaisaka.Library.Management.System.mapper.UserMapper;
import com.mwaisaka.Library.Management.System.repository.UserRepository;
import com.mwaisaka.Library.Management.System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUserById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with Id: '%s' not found", userId));

        return userMapper.toDto(user);
    }

    @Override
    public Page<UserResponse> getAllUsers() {

        Page<UserResponse> list = (Page<UserResponse>) userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
        return list;

    }



    @Override
    public UserResponse updateUser(UUID userId, UserRequest userRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: '%s' not found", userId));

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());

        User savedUser = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(UUID userId) {

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with Id: '%s' not found", userId));

        userRepository.delete(userToDelete);
    }


}
