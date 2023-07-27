package com.unindra.ngrancang.dto.requests;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssuePriority;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.enumeration.IssueType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateIssueRequest {
    @NotNull
    private UUID projectId;

    @Nullable
    private UUID sprintId;

    @NotNull
    private String type;

    @NotNull
    private String priority;

    @NotNull
    private String status;

    @NotNull
    @Max(value = 255, message = "Summary to long")
    private String summary;

    // @Nullable
    private MultipartFile[] attachments;
    
    @Nullable
    private String description;
    
    @NotNull
    private UUID assignee;
    
    @Nullable
    private UUID epicId;

    private Integer storyPoint;
}
