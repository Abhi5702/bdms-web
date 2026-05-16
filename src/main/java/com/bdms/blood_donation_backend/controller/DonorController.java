package com.bdms.blood_donation_backend.controller;

import com.bdms.blood_donation_backend.dto.request.DonorProfileRequest;
import com.bdms.blood_donation_backend.dto.response.ApiResponse;
import com.bdms.blood_donation_backend.dto.response.DonorProfileResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.service.DonorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;

    // POST /api/donors/profile — create donor profile
    @PostMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<DonorProfileResponse>> createProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DonorProfileRequest request) {

        DonorProfileResponse response = donorService.createProfile(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Donor profile created", response));
    }

    // GET /api/donors/profile — get my profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<DonorProfileResponse>> getMyProfile(
            @AuthenticationPrincipal User user) {

        DonorProfileResponse response = donorService.getMyProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Donor profile fetched", response));
    }

    // PUT /api/donors/profile — update my profile
    @PutMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<DonorProfileResponse>> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DonorProfileRequest request) {

        DonorProfileResponse response = donorService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Donor profile updated", response));
    }

    // PATCH /api/donors/availability — toggle availability
    @PatchMapping("/availability")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<DonorProfileResponse>> toggleAvailability(
            @AuthenticationPrincipal User user) {

        DonorProfileResponse response = donorService.toggleAvailability(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Availability updated", response));
    }

    // GET /api/donors/search?bloodType=O_POS&city=Mumbai
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<List<DonorProfileResponse>>> searchDonors(
            @RequestParam(required = false) BloodType bloodType,
            @RequestParam(required = false) String city) {

        List<DonorProfileResponse> response = donorService.searchDonors(bloodType, city);
        return ResponseEntity.ok(ApiResponse.success("Donors fetched", response));
    }

    // GET /api/donors — get all donors (admin only)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<DonorProfileResponse>>> getAllDonors() {

        List<DonorProfileResponse> response = donorService.getAllDonors();
        return ResponseEntity.ok(ApiResponse.success("All donors fetched", response));
    }
}