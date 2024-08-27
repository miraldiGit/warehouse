package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.TruckBookingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RepositoryTruckBookingDate extends JpaRepository<TruckBookingDate, Long> {

    @Modifying
    @Query(value = "DELETE FROM t_truck_booking_date WHERE booking_date = :bookingDate", nativeQuery = true)
    void deleteAllByBookingDate(LocalDate bookingDate);
}
