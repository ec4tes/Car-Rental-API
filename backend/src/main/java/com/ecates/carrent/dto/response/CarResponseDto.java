package com.ecates.carrent.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponseDto {

    private Long id;

    private String model;

    private String plateNumber;

    private Integer year;

    private BigDecimal dailyPrice;

    private String brandName;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
