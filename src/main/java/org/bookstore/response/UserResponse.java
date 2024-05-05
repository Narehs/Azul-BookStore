package org.bookstore.response;

import lombok.*;
import org.bookstore.model.Role;

import java.util.Set;

@Getter
@Setter
public class UserResponse extends BaseResponse{
    private String name;

    private String username;

    private boolean isEnabled;

    @NonNull
    private Set<Role> roles;
}
