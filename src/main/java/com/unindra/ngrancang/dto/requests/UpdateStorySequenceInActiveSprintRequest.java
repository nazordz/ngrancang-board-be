package com.unindra.ngrancang.dto.requests;

import lombok.Data;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.dto.responses.StoryResponse;

import jakarta.validation.constraints.NotEmpty;
/**
 * UpdateStorySequence
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateStorySequenceInActiveSprintRequest {

    @NotEmpty
    List<StoryResponse> stories;

    UUID sprintId;
}