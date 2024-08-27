package com.miraldi.warehouse.dto.userDto;

import com.miraldi.warehouse.utils.Role;
import lombok.Data;

@Data
public class UserDetailsDto {

        private Role role;
        private String username;
        private String lastName;
        private String firstName;
        private String email;
        private String city;
        private int postalCode;
        private String country;
}
