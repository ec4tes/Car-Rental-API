package com.ecates.carrent.repository;

import com.ecates.carrent.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;




public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByNameIgnoreCase(String name);


}
