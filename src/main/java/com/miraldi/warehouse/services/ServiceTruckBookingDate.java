package com.miraldi.warehouse.services;

import com.miraldi.warehouse.repositories.RepositoryTruckBookingDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ServiceTruckBookingDate {

    private final RepositoryTruckBookingDate repositoryTruckBookingDate;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void deleteYesterdayTruckBookingDate(){
        repositoryTruckBookingDate.deleteAllByBookingDate(LocalDate.now().minusDays(1));

    }
}
