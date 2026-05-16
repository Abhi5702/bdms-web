package com.bdms.blood_donation_backend.controller;

import com.bdms.blood_donation_backend.dto.request.HospitalRequest;
import com.bdms.blood_donation_backend.dto.response.ApiResponse;
import com.bdms.blood_donation_backend.dto.response.HospitalResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.service.HospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    // POST /api/hospitals/profile
    @PostMapping("/profile")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<HospitalResponse>> createProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody HospitalRequest request) {

        HospitalResponse response = hospitalService.createProfile(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Hospital profile created", response));
    }

    // GET /api/hospitals/profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<HospitalResponse>> getMyProfile(
            @AuthenticationPrincipal User user) {

        HospitalResponse response = hospitalService.getMyProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Hospital profile fetched", response));
    }

    // PUT /api/hospitals/profile
    @PutMapping("/profile")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<HospitalResponse>> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody HospitalRequest request) {

        HospitalResponse response = hospitalService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Hospital profile updated", response));
    }

    // GET /api/hospitals — all hospitals (any authenticated user)
    @GetMapping
    public ResponseEntity<ApiResponse<List<HospitalResponse>>> getAllHospitals() {

        List<HospitalResponse> response = hospitalService.getAllHospitals();
        return ResponseEntity.ok(ApiResponse.success("Hospitals fetched", response));
    }
}