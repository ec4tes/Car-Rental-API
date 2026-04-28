package com.ecates.carrent.repository;

import com.ecates.carrent.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Long> {

    boolean existsByPlateNumberIgnoreCase(String plateNumber);


}
