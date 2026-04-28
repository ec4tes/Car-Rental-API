package com.ecates.carrent.mapper;

import com.ecates.carrent.dto.request.CarRequestDto;
import com.ecates.carrent.dto.response.CarResponseDto;
import com.ecates.carrent.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toEntity(CarRequestDto carRequestDto);

    @Mapping(source = "brand.name",target = "brandName")
    @Mapping(source = "category.name",target = "categoryName")
    CarResponseDto toCarResponseDto(Car car);

    List<CarResponseDto> toCarResposeDtoList(List<Car> cars);

}
