package com.bdms.blood_donation_backend.dto.request;

import com.bdms.blood_donation_backend.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {

    @NotNull(message = "Role is required")
    private UserRole role;
}