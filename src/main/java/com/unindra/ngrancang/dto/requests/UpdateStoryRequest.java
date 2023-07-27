package com.unindra.ngrancang.dto.requests;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssuePriority;
import com.unindra.ngrancang.enumeration.IssueStatus;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateStoryRequest {
    private UUID assigneeId;
    private String description;    
    private UUID epicId;
    private IssuePriority priority;
    private UUID sprint;
    private IssueStatus status;
    private int storyPoint;
    private String summary;
}
