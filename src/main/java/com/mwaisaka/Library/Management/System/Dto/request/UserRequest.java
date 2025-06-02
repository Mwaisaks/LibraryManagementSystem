package com.mwaisaka.Library.Management.System.Dto.request;

import com.mwaisaka.Library.Management.System.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String name;

    @Email
    private String email;

    @Size(min = 8)
    private String password;
    private UserRole role;
}

//It's interesting that I'm interacting with validation for the first time