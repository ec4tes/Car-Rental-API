package com.ecates.carrent.repository;

import com.ecates.carrent.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car,Long> {

    boolean existsByPlateNumberIgnoreCase(String plateNumber);

    @Query("SELECT c FROM Car c WHERE LOWER(c.brand.name) = LOWER(:brandName)")
    Page<Car> findCarsByBrandName(@Param("brandName") String brandName, Pageable pageable);


}
