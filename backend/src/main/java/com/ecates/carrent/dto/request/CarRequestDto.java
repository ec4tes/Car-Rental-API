package com.ecates.carrent.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequestDto {

    @NotBlank
    private String model;

    @NotBlank
    private String plateNumber;

    @NotNull
    private Integer year;

    @NotNull
    @Positive
    private BigDecimal dailyPrice;

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;
}
