package com.unindra.ngrancang.dto.requests;

import java.util.List;

import lombok.Data;

@Data
public class CreateEpicsRequest {
    private List<EpicRequest> epics;
}
