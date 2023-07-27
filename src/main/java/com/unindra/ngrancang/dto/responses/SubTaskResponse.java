package com.unindra.ngrancang.dto.responses;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubTaskResponse {
    private UUID id;

    private String key;

    private UUID userId;

    @Nullable
    private User user;
    
    private UUID storyId;
    
    @Nullable
    private StoryResponse story;
    
    private String description;
    
    private IssueStatus status;
    
    @Nullable
    private UUID assigneeId;
    
    @Nullable
    private UserResponse assignee;
    
    private int sequence;
    
    private Timestamp createdAt;
    
    private Timestamp updatedAt;

    @Nullable
    private Timestamp deletedAt;
}
