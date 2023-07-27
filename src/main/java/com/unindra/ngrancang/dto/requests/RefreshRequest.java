package com.unindra.ngrancang.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotEmpty
    private String token;
}
