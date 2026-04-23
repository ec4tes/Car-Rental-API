package com.ecates.carrent.mapper;

import com.ecates.carrent.dto.request.BrandRequestDto;
import com.ecates.carrent.dto.response.BrandResponseDto;
import com.ecates.carrent.entity.Brand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toEntity (BrandRequestDto brandRequestDto);

    BrandResponseDto toBrandResponseDto(Brand brand);

    List<BrandResponseDto>  toBrandResponseDtoList(List<Brand> brands);



}
