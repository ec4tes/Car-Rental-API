package com.ecates.carrent.service;

import com.ecates.carrent.dto.request.RentalRequestDto;
import com.ecates.carrent.dto.response.RentalResponseDto;

import java.util.List;

public interface RentalService {

    RentalResponseDto createRental(RentalRequestDto rentalRequestDto);

    List<RentalResponseDto> getAllRental();

    RentalResponseDto getRentalById(Long id);

    RentalResponseDto completeRental(Long id);

    RentalResponseDto cancelRental(Long id);



}
