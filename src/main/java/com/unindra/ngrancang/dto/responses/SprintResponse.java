package com.unindra.ngrancang.dto.responses;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SprintResponse {
    private UUID id;

    private UUID userId;
    
    @Nullable
    private UserResponse user;

    private UUID projectId;
    
    @Nullable
    private ProjectResponse project;
    
    private String sprintName;
    
    private String sprintGoal;
    
    private Date startDate;
    
    private Date endDate;

    private int sequence;

    @JsonProperty("is_running")
    private boolean isRunning;

    private Date actualStartDate;
    
    private Date actualEndDate;
    
    private List<StoryResponse> stories = new ArrayList<>();

    private Integer planStoryPoint;

    private Integer actualStoryPoint;
    
    private Timestamp createdAt;

    private Timestamp updatedAt;
    
    @Nullable
    private Timestamp deletedAt;
}
