package com.unindra.ngrancang.dto.responses;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectResponse {
    private UUID id;
    private String key;
    private String name;
    private String avatar;
    private String description;
    private String userId;
    @Nullable
    private UserResponse user;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @Nullable
    private Timestamp deletedAt;
}
