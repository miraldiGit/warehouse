package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.Truck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryTruck extends JpaRepository<Truck, Long> {

    Optional<Truck> findById(Long id);

    Page<Truck> findAll(Specification<Truck> spec, Pageable pageable);

    boolean existsByChassisNumber(String chassisNumber);

    boolean existsByLicensePlate(String licencePlate);


}
