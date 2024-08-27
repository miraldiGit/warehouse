package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.TruckBookingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepositoryTruckBookingDate extends JpaRepository<TruckBookingDate, Long> {

    List<TruckBookingDate> findAllByBookingDate(LocalDate bookingDate);

    void deleteAllByBookingDate(LocalDate bookingDate);
}
