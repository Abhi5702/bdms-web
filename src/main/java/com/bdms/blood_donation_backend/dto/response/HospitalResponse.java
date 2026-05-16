package com.bdms.blood_donation_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalResponse {
    private Long id;
    private Long userId;
    private String hospitalName;
    private String city;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}