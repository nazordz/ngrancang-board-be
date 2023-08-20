package com.unindra.ngrancang.dto.responses;

import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssueStatus;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActiveSprintLogResponse {
    private UUID id;

    private UUID storyId;

    private StoryResponse story;

    private UUID sprintId;

    private SprintResponse sprint;

    private Integer storyPoint;

    private IssueStatus status;

    private boolean isStart;
    
    private boolean isEnd;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
