package org.bookstore.mapper;

import org.bookstore.dto.UserDto;
import org.bookstore.model.Role;
import org.bookstore.model.User;
import org.bookstore.response.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User entity);

    default Role map(GrantedAuthority authority) {
        return Role.valueOf(authority.getAuthority());
    }

    User toEntity(UserDto entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updatePartial(@MappingTarget User entity, UserDto dto);

    default Page<UserResponse> toResponsePage(Page<User> userPage) {
        return userPage.map(this::toResponse);
    }

}
