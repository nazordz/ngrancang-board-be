package com.unindra.ngrancang.dto.requests;

import lombok.Data;

/**
 * LoginRequest
 */
@Data
public class LoginRequest {
    private String email;

    private String password;
}