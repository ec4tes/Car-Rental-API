package com.ecates.carrent.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String driverLicenseNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
