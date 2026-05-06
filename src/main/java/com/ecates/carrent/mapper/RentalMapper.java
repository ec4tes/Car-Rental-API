package com.ecates.carrent.mapper;

import com.ecates.carrent.dto.response.RentalResponseDto;
import com.ecates.carrent.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    @Mapping(
            target = "customerFullName",
            expression = "java(rental.getCustomer().getFirstName() + \"\" + rental.getCustomer().getLastName())"
    )
    @Mapping(source = "car.plateNumber", target = "carPlateNumber")
    @Mapping(source = "car.model", target = "carModel")

    RentalResponseDto toRentalResponseDto(Rental rental);

    List<RentalResponseDto> toRentalResponseDtoList(List<Rental> rentals);


}
