package com.unindra.ngrancang.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class UpdateSubTasksSequence {
    private List<ListSubTaskSequenceRequest> subtasks;
}
 