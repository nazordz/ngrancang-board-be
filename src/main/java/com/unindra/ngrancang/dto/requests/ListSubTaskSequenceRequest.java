package com.unindra.ngrancang.dto.requests;

import java.util.UUID;

import lombok.Data;

@Data
public class ListSubTaskSequenceRequest {
    private UUID id;
    private String summary;
    private boolean draft;
    private int sequence;
}
