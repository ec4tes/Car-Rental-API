package com.ecates.carrent.service.impl;

import com.ecates.carrent.dto.request.BrandRequestDto;
import com.ecates.carrent.dto.response.BrandResponseDto;
import com.ecates.carrent.entity.Brand;
import com.ecates.carrent.mapper.BrandMapper;
import com.ecates.carrent.repository.BrandRepository;
import com.ecates.carrent.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;


    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }


    private boolean existsByName (String name){

        return brandRepository.existsByNameIgnoreCase(name);
    }


    private Brand findBrandByIdOrThrow(Long id){
        return brandRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Belirtilen id'ye sahip marka bulunamadı."));
    }


    @Override
    public BrandResponseDto createBrand(BrandRequestDto brandRequestDto) {
        String brandName = brandRequestDto.getName().trim();

        if (existsByName(brandName)){
            throw new RuntimeException("marka mevcut");
        }
        Brand brand = brandMapper.toEntity(brandRequestDto);
        brand.setName(brandName);

        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toBrandResponseDto(savedBrand);

    }


    @Override
    public List<BrandResponseDto> getAllBrands() {
        List<Brand> brandList = brandRepository.findAll();
        return brandMapper.toBrandResponseDtoList(brandList);
    }


    @Override
    public BrandResponseDto getBrandById(Long id) {
        Brand brand = findBrandByIdOrThrow(id);
        return brandMapper.toBrandResponseDto(brand);
    }


    @Override
    public BrandResponseDto updateBrand(Long id, BrandRequestDto brandRequestDto) {
        Brand brand = findBrandByIdOrThrow(id);
        String normalizedName = brandRequestDto.getName().trim();

        if (! brand.getName().equalsIgnoreCase(normalizedName) &&existsByName(normalizedName)){
            throw new RuntimeException("marka mevcut");
        }
        brand.setName(normalizedName);
        Brand updatedBrand = brandRepository.save(brand);
        return brandMapper.toBrandResponseDto(updatedBrand);
    }


    @Override
    public void deleteBrandById(Long id) {
        Brand brand = findBrandByIdOrThrow(id);
        brandRepository.delete(brand);
    }
}
