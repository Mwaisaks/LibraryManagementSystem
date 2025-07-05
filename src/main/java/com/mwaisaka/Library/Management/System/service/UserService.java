package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<UserDto> getAllUsers();
}