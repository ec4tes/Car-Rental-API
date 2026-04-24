package com.ecates.carrent.mapper;

import com.ecates.carrent.dto.request.CustomerRequestDto;
import com.ecates.carrent.dto.response.CustomerResponseDto;
import com.ecates.carrent.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponseDto toCustomerResponseDto(Customer customer);

    Customer toEntity(CustomerRequestDto customerRequestDto);

    List<CustomerResponseDto> toCustomerResponseDtoList(List<Customer> customers);

}
