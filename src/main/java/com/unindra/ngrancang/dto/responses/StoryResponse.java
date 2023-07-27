package com.unindra.ngrancang.dto.responses;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssuePriority;
import com.unindra.ngrancang.enumeration.IssueStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class StoryResponse {
    private UUID id;
    
    private String key;
    
    private UUID userId;
    
    @Nullable
    private UserResponse user;
    
    @Nullable
    private UUID sprintId;

    @Nullable
    private SprintResponse sprint;
    
    private UUID projectId;
    
    @Nullable
    private ProjectResponse project;
    
    private IssuePriority priority;
    
    private IssueStatus status;
    
    private String summary;
    
    @Nullable
    private UUID assigneeId;
    
    @Nullable
    private UserResponse assignee;
    
    private List<String> attachments;
    
    private String description;
    
    private Integer storyPoint;
    
    @Nullable
    private UUID epicId;
    
    @Nullable
    private EpicResponse epic;

    private int sequence;

    private List<SubTaskResponse> subTasks = new ArrayList<>();

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Nullable
    private Timestamp deletedAt;
}
