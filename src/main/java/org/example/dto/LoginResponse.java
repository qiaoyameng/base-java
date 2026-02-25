package org.example.dto;

import lombok.Data;
import org.example.enums.RoleType;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private String realName;
    private RoleType role;
    private Long userId;
    private Long customerId;
}
