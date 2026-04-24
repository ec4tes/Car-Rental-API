package com.ecates.carrent.repository;

import com.ecates.carrent.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByDriverLicenseNumberIgnoreCase(String driverLicenseNumber);

}
