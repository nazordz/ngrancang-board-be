package com.unindra.ngrancang.dto.requests;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssueStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActiveSprintLogRequest {
    @NotEmpty
    private UUID storyId;

    @NotEmpty
    private UUID sprintId;

    @NotEmpty
    @Positive
    private Integer storyPoint;

    @NotEmpty
    private IssueStatus status;

    private boolean isStart = false;

    private boolean isEnd = false;
}
