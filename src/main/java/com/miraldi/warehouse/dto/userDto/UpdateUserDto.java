package com.miraldi.warehouse.dto.userDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateUserDto {

    private String lastName;
    private String firstName;
    private String city;
    @Min(1000)
    @Max(999999)
    private int postalCode;
    private String country;
}