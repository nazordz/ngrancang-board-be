package com.unindra.ngrancang.dto.requests;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserRequest {
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String position;
    
    @NotEmpty
    @Email
    private String email;
    
    @NotEmpty
    @NumberFormat
    private String phone;

    @Nullable
    private String password;
}
