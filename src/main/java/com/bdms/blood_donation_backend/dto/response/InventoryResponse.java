package com.bdms.blood_donation_backend.dto.response;

import com.bdms.blood_donation_backend.enums.BloodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long id;
    private Long hospitalId;
    private String hospitalName;
    private BloodType bloodType;
    private int unitsAvailable;
    private LocalDate expiryDate;
    private LocalDateTime updatedAt;
}