package com.mwaisaka.Library.Management.System.domain.dto.request;

import com.mwaisaka.Library.Management.System.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String firstName;

    private String lastName;

    @Email
    private String email;

    @Size(min = 8)
    private String password;
    private UserRole role;
}
