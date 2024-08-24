package com.miraldi.warehouse.services;

import com.miraldi.warehouse.dto.converter.TruckConverter;
import com.miraldi.warehouse.dto.truckDto.CreateTruckDto;
import com.miraldi.warehouse.dto.truckDto.TruckDto;
import com.miraldi.warehouse.dto.truckDto.UpdateTruckDto;
import com.miraldi.warehouse.entities.Truck;
import com.miraldi.warehouse.repositories.RepositoryTruck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.miraldi.warehouse.repositories.specifications.SpecificationTruck.hasChassisNumberLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationTruck.hasDelivered;
import static com.miraldi.warehouse.repositories.specifications.SpecificationTruck.hasItemsQuantityInTruck;
import static com.miraldi.warehouse.repositories.specifications.SpecificationTruck.hasLicensePlateLike;


@Service
@RequiredArgsConstructor
public class ServiceTruck {

    private final RepositoryTruck repositoryTruck;
    private final TruckConverter truckConverter;

    public Page<TruckDto> searchTrucks(TruckRequestFilter truckRequestFilter, Pageable pageable) {

        Page<Truck> pageTruck;

        Specification<Truck> truckSpecification =
                Specification.where(hasChassisNumberLike(truckRequestFilter.getChassisNumber()))
                .and(hasLicensePlateLike(truckRequestFilter.getLicensePlate()))
                .and(hasItemsQuantityInTruck(truckRequestFilter.getItemsQuantityInTruck()))
                .and(hasDelivered(truckRequestFilter.getDelivered()));

        pageTruck = repositoryTruck.findAll(truckSpecification, pageable);

        return pageTruck.map(truckConverter::convertTruckToTruckDto);
    }

    public TruckDto createTruck(CreateTruckDto createTruckDto){
        if(!repositoryTruck.existsByChassisNumber(createTruckDto.getChassisNumber()) &&
           !repositoryTruck.existsByLicensePlate(createTruckDto.getLicensePlate())) {
            Truck truck = repositoryTruck.save(truckConverter.convertCreateTruckDtoToTruck(createTruckDto));
            return truckConverter.convertTruckToTruckDto(truck);
        }
        else throw new IncorrectDataException("A truck with the same chassis number or license plate already exists.");
    }

    public void updateTruck(Long truckId, UpdateTruckDto updateTruckDto){
        var truck = repositoryTruck.findById(truckId)
                .orElseThrow(ResourceNotFoundException::new);

        if(updateTruckDto.getItemsQuantityInTruck() != null &&
           updateTruckDto.getItemsQuantityInTruck() >=0 &&
           updateTruckDto.getDelivered() != null){
            truck = truckConverter.convertUpdateTruckDtoToTruck(updateTruckDto);
            repositoryTruck.save(truck);
        }
        else throw new IncorrectDataException("A truck must not have itemsQuantityInTruck as negative or null " +
                "and/or delivered as null.");
    }

    public void deleteTruck(Long truckId){
        var truck = repositoryTruck.findById(truckId)
                .orElseThrow(ResourceNotFoundException::new);
        truck.setDeleted(true);
        repositoryTruck.save(truck);
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TruckRequestFilter {

        private String chassisNumber;
        private String licensePlate;
        private Integer itemsQuantityInTruck;
        private Boolean delivered;
    }
}
