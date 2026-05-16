package com.bdms.blood_donation_backend.service;

import com.bdms.blood_donation_backend.dto.request.UpdateRoleRequest;
import com.bdms.blood_donation_backend.dto.response.UserResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.exceptions.BadRequestException;
import com.bdms.blood_donation_backend.exceptions.ResourceNotFoundException;
import com.bdms.blood_donation_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ── Get all users ──────────────────────────────────────
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    // ── Get user by ID ─────────────────────────────────────
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        return mapToResponse(user);
    }

    // ── Get my profile ─────────────────────────────────────
    public UserResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToResponse(user);
    }

    // ── Update role ────────────────────────────────────────
    public UserResponse updateRole(Long id, UpdateRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setRole(request.getRole());
        userRepository.save(user);
        return mapToResponse(user);
    }

    // ── Delete user ────────────────────────────────────────
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // ── Toggle verified status ─────────────────────────────
    public UserResponse toggleVerified(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setVerified(!user.isVerified());
        userRepository.save(user);
        return mapToResponse(user);
    }

    // ── Mapper ─────────────────────────────────────────────
    public UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .isVerified(user.isVerified())
                .build();
    }
}