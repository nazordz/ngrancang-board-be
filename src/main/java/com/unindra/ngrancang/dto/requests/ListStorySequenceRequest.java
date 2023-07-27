package com.unindra.ngrancang.dto.requests;

import java.util.UUID;

import lombok.Data;

@Data
public class ListStorySequenceRequest {
    private UUID id;
    private String description;
    private boolean draft;
    private int sequence;
}
