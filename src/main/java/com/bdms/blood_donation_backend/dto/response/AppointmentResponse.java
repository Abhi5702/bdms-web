package com.bdms.blood_donation_backend.dto.response;

import com.bdms.blood_donation_backend.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long donorId;
    private String donorName;
    private String donorBloodType;
    private Long hospitalId;
    private String hospitalName;
    private Long requestId;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
}