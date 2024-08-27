package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.Truck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryTruck extends JpaRepository<Truck, Long> {

    @EntityGraph(attributePaths = {
            "bookingDates"
    })
    Optional<Truck> findById(Long id);

    Page<Truck> findAll(Specification<Truck> spec, Pageable pageable);

    @EntityGraph(attributePaths = {
            "bookingDates"
    })
    List<Truck> findAll();

    boolean existsByChassisNumber(String chassisNumber);

    boolean existsByLicensePlate(String licencePlate);


}
