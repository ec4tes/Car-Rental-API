package com.ecates.carrent.service;

import com.ecates.carrent.dto.request.BrandRequestDto;
import com.ecates.carrent.dto.response.BrandResponseDto;

import java.util.List;

public interface BrandService {

    BrandResponseDto createBrand(BrandRequestDto brandRequestDto);

    List<BrandResponseDto> getAllBrands();

    BrandResponseDto getBrandById(Long id);

    BrandResponseDto updateBrand(Long id, BrandRequestDto brandRequestDto);

    void deleteBrandById(Long id);

}
