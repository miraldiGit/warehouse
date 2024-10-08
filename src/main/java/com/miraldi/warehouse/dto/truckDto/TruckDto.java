package com.miraldi.warehouse.dto.truckDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TruckDto extends CreateTruckDto{
    private Long id;
    private Integer itemsQuantityInTruck;
}
