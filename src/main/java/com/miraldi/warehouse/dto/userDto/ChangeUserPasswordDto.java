package com.miraldi.warehouse.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeUserPasswordDto {

    @NotBlank(message = "Value of field usernameOrEmail is missing")
    private String usernameOrEmail;

    @NotBlank(message = "Value of field oldPassword is missing")
    private String oldPassword;

    @NotBlank(message="Value for field Password is missing")
    @Pattern(regexp = "(?=.*\\d.*)(?=.*[^a-zA-Z0-9].*)(?=.*[A-Z].*).{12,}",
            message = "New Password does not match one ore more of the following criteria:" + "\n" +
                    "1) Does not contain at least one capital letter" + "\n" +
                    "2) Does not contain at least one digit" + "\n" +
                    "3) Does not contain at least one special character" + "\n" +
                    "4) Does not contain at least 12 characters")
    private String newPassword;


}