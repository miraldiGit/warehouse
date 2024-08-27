package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.dto.truckDto.CreateTruckDto;
import com.miraldi.warehouse.dto.truckDto.TruckDto;
import com.miraldi.warehouse.dto.truckDto.UpdateTruckDto;
import com.miraldi.warehouse.services.ServiceTruck;
import com.miraldi.warehouse.services.ServiceTruck.TruckRequestFilter;
import com.miraldi.warehouse.utils.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.miraldi.warehouse.utils.PathsAndStrings.BASE_TRUCK_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.TRUCK_PATH_VARIABLE;

@RestController
@RequestMapping(BASE_TRUCK_PATH)
public class TruckController {

    private final ServiceTruck serviceTruck;
    private final PageableUtil pageableUtil;

    public TruckController(ServiceTruck serviceTruck, PageableUtil pageableUtil){
        this.serviceTruck = serviceTruck;
        this.pageableUtil = pageableUtil;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TruckDto> searchTrucks(@RequestParam(value="chassis-number", required = false)
                                           String chassisNumber,
                                       @RequestParam(value="license-plate", required = false)
                                           String licensePlate,
                                       @RequestParam(value="items-quantity-in-truck", required = false)
                                           Integer itemsQuantityInTruck,
                                       @RequestParam(value="delivered", required = false)
                                           Boolean delivered,
                                       @RequestParam(value = "sort-by", defaultValue = "itemsQuantityInTruck") String sortBy,
                                       @RequestParam(value="sort-direction", defaultValue = "ASC") Sort.Direction sortDirection,
                                       Pageable pageable) {

        var requestFilter = new TruckRequestFilter();
        requestFilter.setChassisNumber(chassisNumber);
        requestFilter.setLicensePlate(licensePlate);
        requestFilter.setItemsQuantityInTruck(itemsQuantityInTruck);
        requestFilter.setDelivered(delivered);

        Sort sort = Sort.by(new Sort.Order(sortDirection, sortBy));
        Pageable pageRequest = pageableUtil.getPageable(pageable, sort);
        return serviceTruck.searchTrucks(requestFilter, pageRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TruckDto addTruck(@RequestBody CreateTruckDto createTruckDto) {
        return serviceTruck.createTruck(createTruckDto);
    }

    @PutMapping(TRUCK_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    public void updateTruck(@PathVariable("truck-id") Long truckId,
                            @RequestBody UpdateTruckDto updateTruckDto) {
        serviceTruck.updateTruck(truckId, updateTruckDto);
    }

    @DeleteMapping(TRUCK_PATH_VARIABLE)
    public void deleteTruck(@PathVariable("truck-id") Long truckId) {
        serviceTruck.deleteTruck(truckId);
    }



}
