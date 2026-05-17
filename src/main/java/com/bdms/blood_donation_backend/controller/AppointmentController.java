package com.bdms.blood_donation_backend.controller;

import com.bdms.blood_donation_backend.dto.request.AppointmentRequest;
import com.bdms.blood_donation_backend.dto.response.ApiResponse;
import com.bdms.blood_donation_backend.dto.response.AppointmentResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.enums.AppointmentStatus;
import com.bdms.blood_donation_backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // POST /api/appointments — donor books appointment
    @PostMapping
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AppointmentRequest request) {

        AppointmentResponse response = appointmentService
                .bookAppointment(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked", response));
    }

    // GET /api/appointments/my — donor's own appointments
    @GetMapping("/my")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointments(
            @AuthenticationPrincipal User user) {

        List<AppointmentResponse> response = appointmentService
                .getMyAppointments(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Appointments fetched", response));
    }

    // GET /api/appointments/hospital — hospital sees its appointments
    @GetMapping("/hospital")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getHospitalAppointments(
            @AuthenticationPrincipal User user) {

        List<AppointmentResponse> response = appointmentService
                .getHospitalAppointments(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Appointments fetched", response));
    }

    // GET /api/appointments — all (admin)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {

        List<AppointmentResponse> response = appointmentService.getAllAppointments();
        return ResponseEntity.ok(ApiResponse.success("All appointments fetched", response));
    }

    // PATCH /api/appointments/{id}/status?status=COMPLETED
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {

        AppointmentResponse response = appointmentService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated", response));
    }

    // PATCH /api/appointments/{id}/cancel — donor cancels
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        AppointmentResponse response = appointmentService
                .cancelAppointment(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled", response));
    }
}