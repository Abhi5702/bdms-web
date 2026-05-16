package com.bdms.blood_donation_backend.service;

import com.bdms.blood_donation_backend.dto.request.InventoryRequest;
import com.bdms.blood_donation_backend.dto.response.InventoryResponse;
import com.bdms.blood_donation_backend.entity.Hospital;
import com.bdms.blood_donation_backend.entity.Inventory;
import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.exceptions.ResourceNotFoundException;
import com.bdms.blood_donation_backend.repository.HospitalRepository;
import com.bdms.blood_donation_backend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final HospitalRepository hospitalRepository;

    // ── Add or update inventory ────────────────────────────
    // If (hospital, bloodType) already exists → update units
    // If not → create new row
    public InventoryResponse addOrUpdate(Long userId, InventoryRequest request) {

        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        Inventory inventory = inventoryRepository
                .findByHospitalIdAndBloodType(hospital.getId(), request.getBloodType())
                .orElse(Inventory.builder()
                        .hospital(hospital)
                        .bloodType(request.getBloodType())
                        .build());

        inventory.setUnitsAvailable(request.getUnitsAvailable());
        inventory.setExpiryDate(request.getExpiryDate());

        inventoryRepository.save(inventory);
        return mapToResponse(inventory);
    }

    // ── Get my hospital inventory ──────────────────────────
    public List<InventoryResponse> getMyInventory(Long userId) {

        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        return inventoryRepository.findByHospitalId(hospital.getId())
                .stream().map(this::mapToResponse).toList();
    }

    // ── Get inventory by hospital ID (public) ─────────────
    public List<InventoryResponse> getByHospitalId(Long hospitalId) {
        return inventoryRepository.findByHospitalId(hospitalId)
                .stream().map(this::mapToResponse).toList();
    }

    // ── Get all inventory (admin) ──────────────────────────
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    // ── Check availability for a blood type ───────────────
    public InventoryResponse checkAvailability(Long hospitalId, BloodType bloodType) {
        Inventory inventory = inventoryRepository
                .findByHospitalIdAndBloodType(hospitalId, bloodType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No inventory found for blood type: " + bloodType));
        return mapToResponse(inventory);
    }

    // ── Delete inventory entry ─────────────────────────────
    public void deleteInventory(Long userId, Long inventoryId) {

        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        if (!inventory.getHospital().getId().equals(hospital.getId())) {
            throw new ResourceNotFoundException("Inventory does not belong to your hospital");
        }

        inventoryRepository.delete(inventory);
    }

    // ── Mapper ─────────────────────────────────────────────
    private InventoryResponse mapToResponse(Inventory inv) {
        return InventoryResponse.builder()
                .id(inv.getId())
                .hospitalId(inv.getHospital().getId())
                .hospitalName(inv.getHospital().getHospitalName())
                .bloodType(inv.getBloodType())
                .unitsAvailable(inv.getUnitsAvailable())
                .expiryDate(inv.getExpiryDate())
                .updatedAt(inv.getUpdatedAt())
                .build();
    }
}