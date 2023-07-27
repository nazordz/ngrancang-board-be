package com.unindra.ngrancang.dto.requests;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateSprintRequest {
    @NotEmpty
    private UUID projectId;

    @NotEmpty
    private UUID userId;
    
    @NotEmpty
    private String sprintName;
    
    @NotEmpty
    private String sprintGoal;

    @NotEmpty
    private Date startDate;

    @NotEmpty
    private Date endDate;
}
