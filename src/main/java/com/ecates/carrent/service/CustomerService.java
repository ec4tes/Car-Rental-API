package com.ecates.carrent.service;

import com.ecates.carrent.dto.request.CustomerRequestDto;
import com.ecates.carrent.dto.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);

    List<CustomerResponseDto> getAllCustomer();

    CustomerResponseDto getCustomerById(Long id);

    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto);

    void deleteCustomerById(Long id);

}
