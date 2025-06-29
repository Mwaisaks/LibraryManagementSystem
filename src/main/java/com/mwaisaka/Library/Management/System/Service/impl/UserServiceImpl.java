package com.mwaisaka.Library.Management.System.Service.impl;

import com.mwaisaka.Library.Management.System.Dto.mapper.UserMapper;
import com.mwaisaka.Library.Management.System.Repository.UserRepository;
import com.mwaisaka.Library.Management.System.Service.UserService;
import com.mwaisaka.Library.Management.System.domain.Dto.UserDto;
import com.mwaisaka.Library.Management.System.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();

    }
}
