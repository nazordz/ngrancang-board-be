package com.unindra.ngrancang.dto.responses;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class UserResponse {
    private UUID id;
    private String name;
    private String position;
    private String email;
    private String phone;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<RoleResponse> roles = new ArrayList<RoleResponse>();
    private List<ProjectResponse> projects = new ArrayList<ProjectResponse>();
    private String username;
    public boolean isAccountNonExpired;
    public boolean isAccountNonLocked;
    public boolean isCredentialsNonExpired;
    public boolean isEnabled;
}
