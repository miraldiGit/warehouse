package com.miraldi.warehouse.dto.converter;

import com.miraldi.warehouse.dto.truckDto.CreateTruckDto;
import com.miraldi.warehouse.dto.truckDto.TruckDto;
import com.miraldi.warehouse.dto.truckDto.UpdateTruckDto;
import com.miraldi.warehouse.entities.Truck;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface TruckConverter {

    TruckDto convertTruckToTruckDto(Truck truck);

    Truck convertCreateTruckDtoToTruck(CreateTruckDto createTruckDto);

    void convertUpdateTruckDtoToTruck(UpdateTruckDto updateTruckDto, @MappingTarget Truck truck);
}
