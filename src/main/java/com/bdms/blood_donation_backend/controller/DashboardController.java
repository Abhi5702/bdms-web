package com.bdms.blood_donation_backend.controller;

import com.bdms.blood_donation_backend.dto.response.ApiResponse;
import com.bdms.blood_donation_backend.dto.response.DashboardResponse;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // GET /api/dashboard
    // Returns data based on the logged-in user's role automatically
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @AuthenticationPrincipal User user) {

        DashboardResponse response = dashboardService.getDashboard(user);
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard data fetched", response));
    }
}