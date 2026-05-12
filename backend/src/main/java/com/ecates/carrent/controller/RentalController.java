package com.ecates.carrent.controller;

import com.ecates.carrent.dto.request.RentalRequestDto;
import com.ecates.carrent.dto.response.RentalResponseDto;
import com.ecates.carrent.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<RentalResponseDto> createRental(@Valid @RequestBody RentalRequestDto rentalRequestDto){
        RentalResponseDto createdRental = rentalService.createRental(rentalRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRental);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDto> getRentalById(@PathVariable Long id){
        RentalResponseDto rentalById = rentalService.getRentalById(id);
        return ResponseEntity.ok(rentalById);
    }

    @GetMapping
    public ResponseEntity<List<RentalResponseDto>> getAllRenatal(){
        List<RentalResponseDto> allRental = rentalService.getAllRental();
        return ResponseEntity.ok(allRental);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<RentalResponseDto> complateRental(@PathVariable Long id){
        RentalResponseDto rental = rentalService.completeRental(id);
        return ResponseEntity.ok(rental);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RentalResponseDto> cancelRental(@PathVariable Long id){
        RentalResponseDto rental = rentalService.cancelRental(id);
        return ResponseEntity.ok(rental);
    }


}
