package com.unindra.ngrancang.dto.responses;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoleResponse {
    private UUID id;
    private String name;
    private List<UserResponse> users = new ArrayList<UserResponse>();
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
