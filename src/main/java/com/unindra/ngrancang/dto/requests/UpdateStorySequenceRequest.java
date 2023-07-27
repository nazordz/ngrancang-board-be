package com.unindra.ngrancang.dto.requests;

import lombok.Data;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
/**
 * UpdateStorySequence
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateStorySequenceRequest {

    @NotEmpty
    List<ListStorySequenceRequest> stories;

    UUID sprintId;
}