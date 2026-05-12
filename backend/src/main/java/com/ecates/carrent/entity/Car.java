package com.ecates.carrent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    @Column(name = "model",nullable = false)
    private String model;

    @Column(name = "plate_Number",nullable = false,unique = true)
    private String plateNumber;

    @Column(name = "year",nullable = false)
    private Integer year;

    @Column(name = "daily_price",nullable = false)
    private BigDecimal dailyPrice;

    @ManyToOne
    @JoinColumn(name = "brand_id",nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;


}
