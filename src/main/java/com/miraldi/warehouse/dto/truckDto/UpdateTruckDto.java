package com.miraldi.warehouse.dto.truckDto;

import com.miraldi.warehouse.dto.truckBookingDate.TruckBookingDateDto;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateTruckDto {
    private Integer itemsQuantityInTruck;
    private Set<TruckBookingDateDto> truckBookingDates;
}
