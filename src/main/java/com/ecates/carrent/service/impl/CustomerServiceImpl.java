package com.ecates.carrent.service.impl;

import com.ecates.carrent.dto.request.CustomerRequestDto;
import com.ecates.carrent.dto.response.CustomerResponseDto;
import com.ecates.carrent.entity.Customer;
import com.ecates.carrent.mapper.CustomerMapper;
import com.ecates.carrent.repository.CustomerRepository;
import com.ecates.carrent.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }


    private Customer findCustomerByIdOrThrow(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Belirtilen id'ye sahip müşteri bulunamadı."));

    }

    private boolean existsByEmail(String email){
        return customerRepository.existsByEmailIgnoreCase(email);
    }

    private boolean existsByPhoneNumber(String phoneNumber){
        return customerRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean existsByDriverLicenseNumber(String driverLicenseNumber){
        return customerRepository.existsByDriverLicenseNumberIgnoreCase(driverLicenseNumber);
    }

    private String normalizeText(String value){
        return value.trim();
    }

    private String normalizeEmail(String email){
        return email.trim().toLowerCase();
    }

    private void validateUniqueFieldsForCreate(String email, String phoneNumber, String driverLicenceNumber){
        if (existsByEmail(email)){
            throw new RuntimeException("Bu email ile kayitli bir musteri var");
        }
        if (existsByPhoneNumber(phoneNumber)){
            throw new RuntimeException("Bu telefon nnumarasi ile kayitli musteri var");
        }
        if (existsByDriverLicenseNumber(driverLicenceNumber)){
            throw new RuntimeException("Bu ehliyet numarasi ile kayitli musteri var");
        }
    }

    private void applyCustomerFields(Customer customer,
                                     String firstName,
                                     String lastName,
                                     String email,
                                     String phoneNumber,
                                     String driverLicenseNumber){
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setDriverLicenseNumber(driverLicenseNumber);
    }



    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        String firstName = normalizeText(customerRequestDto.getFirstName());
        String lastName = normalizeText(customerRequestDto.getLastName());
        String email = normalizeEmail(customerRequestDto.getEmail());
        String phoneNumber = normalizeText(customerRequestDto.getPhoneNumber());
        String driverLicenseNumber = normalizeText(customerRequestDto.getDriverLicenseNumber());

        validateUniqueFieldsForCreate(email,phoneNumber,driverLicenseNumber);

        Customer customer = customerMapper.toEntity(customerRequestDto);

        applyCustomerFields(customer,firstName,lastName,email,phoneNumber,driverLicenseNumber);

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toCustomerResponseDto(savedCustomer);

    }


    @Override
    public List<CustomerResponseDto> getAllCustomer() {
        List<Customer> customerRepositoryAll = customerRepository.findAll();
        return customerMapper.toCustomerResponseDtoList(customerRepositoryAll);
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = findCustomerByIdOrThrow(id);
        return customerMapper.toCustomerResponseDto(customer);
    }

    private void validateUniqueFieldsForUpdate(Customer customer,
                                               String email,
                                               String phoneNumber,
                                               String driverLicenseNumber){
        if (!customer.getEmail().equalsIgnoreCase(email)&& existsByEmail(email)){
            throw  new RuntimeException("Bu email ile kayıtlı müşteri zaten mevcut.");
        }
        if (!customer.getPhoneNumber().equals(phoneNumber) && existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException("Bu telefon numarası ile kayıtlı müşteri zaten mevcut.");
        }

        if (!customer.getDriverLicenseNumber().equalsIgnoreCase(driverLicenseNumber) && existsByDriverLicenseNumber(driverLicenseNumber)) {
            throw new RuntimeException("Bu ehliyet numarası ile kayıtlı müşteri zaten mevcut.");
        }

    }

    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto) {

        Customer customer = findCustomerByIdOrThrow(id);
        String firstName = normalizeText(customerRequestDto.getFirstName());
        String lastName = normalizeText(customerRequestDto.getLastName());
        String email = normalizeEmail(customerRequestDto.getEmail());
        String phoneNumber = normalizeText(customerRequestDto.getPhoneNumber());
        String driverLicenseNumber = normalizeText(customerRequestDto.getDriverLicenseNumber());

        validateUniqueFieldsForUpdate(customer,email,phoneNumber,driverLicenseNumber);

        applyCustomerFields(customer,firstName,lastName,email,phoneNumber,driverLicenseNumber);

        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponseDto(updatedCustomer);
    }

    @Override
    public void deleteCustomerById(Long id) {
        Customer customer = findCustomerByIdOrThrow(id);
        customerRepository.delete(customer);

    }
}
