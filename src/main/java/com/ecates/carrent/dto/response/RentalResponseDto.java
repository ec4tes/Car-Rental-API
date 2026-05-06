package com.ecates.carrent.dto.response;

import com.ecates.carrent.enums.RentalStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalResponseDto {

    private Long id;
    private String customerFullName;
    private String carPlateNumber;
    private String carModel;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private BigDecimal totalPrice;
    private RentalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
