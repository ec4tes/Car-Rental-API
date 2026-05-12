package com.ecates.carrent.controller;

import com.ecates.carrent.dto.request.CustomerRequestDto;
import com.ecates.carrent.dto.response.CustomerResponseDto;
import com.ecates.carrent.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto){
        CustomerResponseDto customer = customerService.createCustomer(customerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers(){
        List<CustomerResponseDto> allCustomer = customerService.getAllCustomer();
        return ResponseEntity.ok(allCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id){
        CustomerResponseDto customerById = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerById);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomers(@PathVariable Long id, @Valid @RequestBody CustomerRequestDto customerRequestDto){
        CustomerResponseDto customerResponseDto = customerService.updateCustomer(id, customerRequestDto);
        return ResponseEntity.ok(customerResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

}
