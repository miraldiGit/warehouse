package com.miraldi.warehouse.dto.userDto;

import com.miraldi.warehouse.utils.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserDto {

    @NotBlank(message = "Value for field Role is missing")
    private Role role;

    @NotBlank(message="Value for field username is missing or the username provided already exists")
    private String username;

    @NotBlank(message="Value for field Password is missing")
    @Pattern(regexp = "(?=.*\\d.*)(?=.*[^a-zA-Z0-9].*)(?=.*[A-Z].*).{12,}",
            message = "Password field is missing or it does " +
                    "not match one ore more of the following criteria: \n" +
                    "1) Does not contain at least one capital letter\n" +
                    "2) Does not contain at least one digit\n" +
                    "3) Does not contain at least one special character\n" +
                    "4) Does not contain at least 12 characters")
    private String password;

    @NotBlank(message="Value for field LastName is missing")
    private String lastName;

    @NotBlank(message="Value for field FirstName is missing")
    private String firstName;

    @NotBlank(message="Value for field Email is missing or the email provided already exists")
    private String email;

    @NotBlank(message="Value for field City is missing")
    private String city;

    @Min(1000)
    @Max(999999)
    private int postalCode;

    @NotBlank(message="Value for field Country is missing")
    private String country;
}