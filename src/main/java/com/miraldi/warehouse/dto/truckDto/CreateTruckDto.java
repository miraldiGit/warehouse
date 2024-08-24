package com.miraldi.warehouse.dto.truckDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTruckDto {
    @NotNull(message = "Chassis number cannot be null")
    private String chassisNumber;
    @NotNull(message = "License plate cannot be null")
    private String licensePlate;
}
