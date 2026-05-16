package com.bdms.blood_donation_backend.controller;

import com.bdms.blood_donation_backend.dto.request.InventoryRequest;
import com.bdms.blood_donation_backend.dto.response.ApiResponse;
import com.bdms.blood_donation_backend.dto.response.InventoryResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // POST /api/inventory — add or update
    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<InventoryResponse>> addOrUpdate(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody InventoryRequest request) {

        InventoryResponse response = inventoryService.addOrUpdate(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Inventory updated", response));
    }

    // GET /api/inventory/my — my hospital inventory
    @GetMapping("/my")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getMyInventory(
            @AuthenticationPrincipal User user) {

        List<InventoryResponse> response = inventoryService.getMyInventory(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Inventory fetched", response));
    }

    // GET /api/inventory/hospital/{hospitalId} — public
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getByHospital(
            @PathVariable Long hospitalId) {

        List<InventoryResponse> response = inventoryService.getByHospitalId(hospitalId);
        return ResponseEntity.ok(ApiResponse.success("Inventory fetched", response));
    }

    // GET /api/inventory/check?hospitalId=1&bloodType=O_POS
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<InventoryResponse>> checkAvailability(
            @RequestParam Long hospitalId,
            @RequestParam BloodType bloodType) {

        InventoryResponse response = inventoryService.checkAvailability(hospitalId, bloodType);
        return ResponseEntity.ok(ApiResponse.success("Availability checked", response));
    }

    // GET /api/inventory — all inventory (admin)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAllInventory() {

        List<InventoryResponse> response = inventoryService.getAllInventory();
        return ResponseEntity.ok(ApiResponse.success("All inventory fetched", response));
    }

    // DELETE /api/inventory/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOSPITAL_STAFF')")
    public ResponseEntity<ApiResponse<Void>> deleteInventory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        inventoryService.deleteInventory(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Inventory deleted", null));
    }
}