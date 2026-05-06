package com.ecates.carrent.repository;

import com.ecates.carrent.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RentalRepository extends JpaRepository<Rental,Long> {

    boolean existsByCarIdAndStartDateLessThanAndEndDateGreaterThan(
            Long carId,
            LocalDate endDate,
            LocalDate startDate
    );

}
