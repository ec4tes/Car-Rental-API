package com.ecates.carrent.service;

import com.ecates.carrent.dto.request.CarRequestDto;
import com.ecates.carrent.dto.response.CarResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CarService {
    CarResponseDto createCar(CarRequestDto carRequestDto);

    List<CarResponseDto> getAllCars();

    CarResponseDto getCarById(Long id);

    CarResponseDto updateCar(Long id, CarRequestDto carRequestDto);

    void  deleteCar(Long id);

    Page<CarResponseDto> getCarPage(int page, int size);

    Page<CarResponseDto> getCarsByBrandName(String brandName, int page, int size);

}
