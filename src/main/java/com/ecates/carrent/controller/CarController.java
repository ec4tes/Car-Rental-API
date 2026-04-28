package com.ecates.carrent.controller;

import com.ecates.carrent.dto.request.CarRequestDto;
import com.ecates.carrent.dto.response.CarResponseDto;
import com.ecates.carrent.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }


    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@Valid @RequestBody CarRequestDto carRequestDto) {
        CarResponseDto createdCar = carService.createCar(carRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        List<CarResponseDto> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Long id) {
        CarResponseDto car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDto> updateCar(
            @PathVariable Long id,
            @Valid @RequestBody CarRequestDto carRequestDto
    ) {
        CarResponseDto updatedCar = carService.updateCar(id, carRequestDto);
        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
