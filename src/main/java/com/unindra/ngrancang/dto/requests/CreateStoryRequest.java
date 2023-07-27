package com.unindra.ngrancang.dto.requests;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateStoryRequest {
    @NotEmpty
    private UUID id;

    @NotEmpty
    private String summary;

    @NotEmpty
    private UUID projectId;
}
