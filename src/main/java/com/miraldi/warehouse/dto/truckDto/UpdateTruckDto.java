package com.miraldi.warehouse.dto.truckDto;

import lombok.Data;

@Data
public class UpdateTruckDto {
    private Integer itemsQuantityInTruck;
    private Boolean delivered;
}
