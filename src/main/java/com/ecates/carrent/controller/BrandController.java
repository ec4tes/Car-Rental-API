package com.ecates.carrent.controller;


import com.ecates.carrent.dto.request.BrandRequestDto;
import com.ecates.carrent.dto.response.BrandResponseDto;
import com.ecates.carrent.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandService brandService;


    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/create")
    public ResponseEntity<BrandResponseDto> createBrands(@Valid @RequestBody BrandRequestDto brandRequestDto){
        BrandResponseDto createdBrand = brandService.createBrand(brandRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBrand);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BrandResponseDto>> getAllBrands(){
        List<BrandResponseDto> allBrands = brandService.getAllBrands();
        return ResponseEntity.ok(allBrands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDto> getByIdBrands(@PathVariable Long id){
        BrandResponseDto brandById = brandService.getBrandById(id);
        return ResponseEntity.ok(brandById);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BrandResponseDto> updateBrands(@PathVariable Long id,@Valid @RequestBody BrandRequestDto brandRequestDto){
        BrandResponseDto brandResponseDto = brandService.updateBrand(id, brandRequestDto);
        return ResponseEntity.ok(brandResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBrands(@PathVariable Long id){
        brandService.deleteBrandById(id);
    }


}
