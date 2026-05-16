package com.bdms.blood_donation_backend.dto.response;

import com.bdms.blood_donation_backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private UserRole role;
    private String profileImageUrl;
    private boolean isVerified;
}