package com.bdms.blood_donation_backend.controller;

import com.bdms.blood_donation_backend.dto.request.UpdateRoleRequest;
import com.bdms.blood_donation_backend.dto.response.ApiResponse;
import com.bdms.blood_donation_backend.dto.response.UserResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/users/me — any logged-in user
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(
            @AuthenticationPrincipal User user) {

        UserResponse response = userService.getMyProfile(user.getId());
        return ResponseEntity.ok(
                ApiResponse.success("Profile fetched", response));
    }

    // GET /api/users — SUPER_ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(
                ApiResponse.success("Users fetched", response));
    }

    // GET /api/users/{id} — SUPER_ADMIN only
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id) {

        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(
                ApiResponse.success("User fetched", response));
    }

    // PATCH /api/users/{id}/role — SUPER_ADMIN only
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request) {

        UserResponse response = userService.updateRole(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Role updated successfully", response));
    }

    // PATCH /api/users/{id}/toggle-verified — SUPER_ADMIN only
    @PatchMapping("/{id}/toggle-verified")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> toggleVerified(
            @PathVariable Long id) {

        UserResponse response = userService.toggleVerified(id);
        return ResponseEntity.ok(
                ApiResponse.success("Verified status toggled", response));
    }

    // DELETE /api/users/{id} — SUPER_ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully", null));
    }
}