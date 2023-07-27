package com.unindra.ngrancang.dto.responses;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.model.Project;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EpicResponse {

    private UUID id;

    private String key;

    private UUID userId;
    
    @Nullable
    private UserResponse user;

    private UUID projectId;

    @Nullable
    private ProjectResponse project;

    private String summary;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Nullable
    private Timestamp deletedAt;
}
