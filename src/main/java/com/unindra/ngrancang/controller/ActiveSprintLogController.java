package com.unindra.ngrancang.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.ngrancang.dto.requests.ActiveSprintLogRequest;
import com.unindra.ngrancang.dto.responses.ActiveSprintLogResponse;
import com.unindra.ngrancang.model.ActiveSprintLog;
import com.unindra.ngrancang.repository.ActiveSprintLogRepository;

@RestController
@RequestMapping(path = "/api/active-sprint-log")
public class ActiveSprintLogController {
    
    @Autowired
    private ActiveSprintLogRepository activeSprintLogRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping
    public ResponseEntity<ActiveSprintLogResponse> create(@RequestBody ActiveSprintLogRequest request) {
        ActiveSprintLog activeSprintLog = modelMapper.map(request, ActiveSprintLog.class);
        ActiveSprintLog savedData = activeSprintLogRepository.save(activeSprintLog);
        ActiveSprintLogResponse response = modelMapper.map(savedData, ActiveSprintLogResponse.class);
        return ResponseEntity.ok(response);
    }
}
