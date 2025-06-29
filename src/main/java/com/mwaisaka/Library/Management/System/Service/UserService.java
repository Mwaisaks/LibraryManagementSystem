package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.domain.Dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
}