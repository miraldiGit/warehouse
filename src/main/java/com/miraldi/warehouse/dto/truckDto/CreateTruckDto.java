package com.miraldi.warehouse.dto.truckDto;

import lombok.Data;

@Data
public class CreateTruckDto {
    private String chassisNumber;
    private String licensePlate;
    private Integer itemsQuantityInTruck;
}
