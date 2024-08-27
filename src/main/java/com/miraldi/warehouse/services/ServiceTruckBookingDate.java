package com.miraldi.warehouse.services;

import com.miraldi.warehouse.repositories.RepositoryTruckBookingDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ServiceTruckBookingDate {

    private final RepositoryTruckBookingDate repositoryTruckBookingDate;

    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteYesterdayTruckBookingDate(){
        repositoryTruckBookingDate.deleteAllByBookingDate(LocalDate.now().minusDays(1));

    }
}
