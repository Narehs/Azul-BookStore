package org.bookstore.dto;

import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bookstore.model.Role;

@Getter
@Setter
public class UserDto {

    private String name;

    private String username;

    @NonNull
    private String password;

    private boolean isEnabled = true;

    @NonNull
    private Set<Role> roles;
}
