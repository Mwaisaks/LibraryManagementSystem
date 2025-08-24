package com.mwaisaka.Library.Management.System.config;


import com.mwaisaka.Library.Management.System.service.FineService;
import com.mwaisaka.Library.Management.System.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final FineService fineService;
    private final ReservationService reservationService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void generateDailyFines(){
        try{
            log.info("Starting daily fine generation process...");
            fineService.generateFinesForOverdueBooks();
            log.info("Daily fine generation process finished.");
        } catch (Exception e){
            log.error("Error during daily fine generation", e);
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void expireReservation(){
        try{
            log.info("Starting reservation expiration process...");
            reservationService.expireReservations();
            log.info("Reservation expiration completed successfully");
        } catch (Exception e){
            log.error("Error during reservation expiration: ",e);
        }
    }
}
