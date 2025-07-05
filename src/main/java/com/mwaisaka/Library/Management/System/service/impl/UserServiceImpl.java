package com.mwaisaka.Library.Management.System.service.impl;

import com.mwaisaka.Library.Management.System.Dto.mapper.UserMapper;
import com.mwaisaka.Library.Management.System.repository.UserRepository;
import com.mwaisaka.Library.Management.System.service.UserService;
import com.mwaisaka.Library.Management.System.domain.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();

    }
}
