package com.mwaisaka.Library.Management.System.domain.dto.response;

import com.mwaisaka.Library.Management.System.domain.enums.UserRole;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
}
