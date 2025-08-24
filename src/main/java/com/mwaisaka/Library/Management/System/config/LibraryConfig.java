package com.mwaisaka.Library.Management.System.config;


import com.mwaisaka.Library.Management.System.domain.models.Fine;
import com.mwaisaka.Library.Management.System.domain.models.Reservation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "library")
@Getter
@Setter
public class LibraryConfig {
    private Fine fine = new Fine();
    private Reservation reservation = new Reservation();
    private Borrowing borrowing = new Borrowing();

    @Getter
    @Setter
    public static class Fine{
        private String dailyRate = "1.00";
        private String maxFine = "50.00";
        private int gracePeriodDays = 0;
    }

    @Getter
    @Setter
    public static class Reservation{
        private int defaultExpirationDays = 3;
        private int availabilityExpirationDays = 2;
        private int maxActiveReservationsPerUser = 5;
    }

    @Getter
    @Setter
    public static class Borrowing{
        private int defaultLoanPeriodDays = 14;
        private int maxBooksPerUser = 5;
    }

}
