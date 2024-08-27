package com.miraldi.warehouse.security;

import com.miraldi.warehouse.utils.Role;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;


@Getter
public class CustomUserDetails extends User {
    private final Long id;
    private final Role role;
    private final String email;

    public CustomUserDetails(com.miraldi.warehouse.entities.User user){
        super(user.getUsername(), user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name())));

        this.id = user.getId();
        this.role = user.getRole();
        this.email = user.getEmail();

    }

}