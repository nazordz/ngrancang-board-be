package com.unindra.ngrancang.dto.requests;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateSubTasksSimpleRequest {
    @NotEmpty
    private UUID id;
    @NotEmpty
    private UUID storyId;
    @NotEmpty
    private String description;
}
