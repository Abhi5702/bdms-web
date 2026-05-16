package com.bdms.blood_donation_backend.controller;

import com.bdms.blood_donation_backend.dto.request.BloodRequestRequest;
import com.bdms.blood_donation_backend.dto.response.ApiResponse;
import com.bdms.blood_donation_backend.dto.response.BloodRequestResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.enums.RequestStatus;
import com.bdms.blood_donation_backend.service.BloodRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    // POST /api/requests
    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<BloodRequestResponse>> createRequest(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BloodRequestRequest request) {

        BloodRequestResponse response = bloodRequestService.createRequest(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Blood request created", response));
    }

    // GET /api/requests/open
    @GetMapping("/open")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DONOR','HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<List<BloodRequestResponse>>> getOpenRequests() {

        List<BloodRequestResponse> response = bloodRequestService.getOpenRequests();
        return ResponseEntity.ok(ApiResponse.success("Open requests fetched", response));
    }

    // GET /api/requests/my
    @GetMapping("/my")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<List<BloodRequestResponse>>> getMyRequests(
            @AuthenticationPrincipal User user) {

        List<BloodRequestResponse> response = bloodRequestService.getMyRequests(user.getId());
        return ResponseEntity.ok(ApiResponse.success("My requests fetched", response));
    }

    // GET /api/requests/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BloodRequestResponse>> getById(
            @PathVariable Long id) {

        BloodRequestResponse response = bloodRequestService.getRequestById(id);
        return ResponseEntity.ok(ApiResponse.success("Request fetched", response));
    }

    // GET /api/requests/search?bloodType=O_POS
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BloodRequestResponse>>> searchByBloodType(
            @RequestParam BloodType bloodType) {

        List<BloodRequestResponse> response = bloodRequestService.searchByBloodType(bloodType);
        return ResponseEntity.ok(ApiResponse.success("Requests fetched", response));
    }

    // PATCH /api/requests/{id}/status?status=FULFILLED
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BloodRequestResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {

        BloodRequestResponse response = bloodRequestService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated", response));
    }

    // PATCH /api/requests/{id}/cancel
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<BloodRequestResponse>> cancelRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        BloodRequestResponse response = bloodRequestService.cancelRequest(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Request cancelled", response));
    }
}