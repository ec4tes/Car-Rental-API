package com.ecates.carrent.service;

import com.ecates.carrent.dto.request.CarRequestDto;
import com.ecates.carrent.dto.response.CarResponseDto;

import java.util.List;

public interface CarService {
    CarResponseDto createCar(CarRequestDto carRequestDto);

    List<CarResponseDto> getAllCars();

    CarResponseDto getCarById(Long id);

    CarResponseDto updateCar(Long id, CarRequestDto carRequestDto);

    void  deleteCar(Long id);

}
