package com.mwaisaka.Library.Management.System.mapper;

import com.mwaisaka.Library.Management.System.domain.dto.request.UserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity (UserRequest userRequest);

    UserResponse toDto (User user);

}
