package com.bdms.blood_donation_backend.dto.response;

import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.enums.RequestStatus;
import com.bdms.blood_donation_backend.enums.UrgencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestResponse {
    private Long id;
    private Long hospitalId;
    private String hospitalName;
    private String hospitalCity;
    private BloodType bloodType;
    private UrgencyLevel urgency;
    private int unitsNeeded;
    private int unitsFulfilled;
    private RequestStatus status;
    private String notes;
    private LocalDateTime requestedAt;
    private LocalDateTime fulfilledAt;
}