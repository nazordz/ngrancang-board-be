package com.unindra.ngrancang.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.unindra.ngrancang.dto.requests.CreateSprintRequest;
import com.unindra.ngrancang.dto.responses.EpicResponse;
import com.unindra.ngrancang.dto.responses.SprintResponse;
import com.unindra.ngrancang.dto.responses.StoryResponse;
import com.unindra.ngrancang.dto.responses.UserResponse;
import com.unindra.ngrancang.model.Sprint;
import com.unindra.ngrancang.repository.SprintRepository;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {
    
    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/project/{project_id}")
    public ResponseEntity<List<SprintResponse>> findSprintByProject(@PathVariable("project_id") UUID projectId) {
        List<Sprint> sprints = sprintRepository.findByProjectId(projectId);
        List<SprintResponse> sprintsResponse = sprints.stream().map(s -> {
            return modelMapper.map(s, SprintResponse.class);
        }).toList();

        return ResponseEntity.ok(sprintsResponse);
    }
    @GetMapping("/project/{project_id}/stories")
    public ResponseEntity<List<SprintResponse>> findSprintByProjectIdWithStories(@PathVariable("project_id") UUID projectId) {
        List<Sprint> sprints = sprintRepository.findByProjectIdWithStoriesOrderBySequenceAsc(projectId);
        List<SprintResponse> sprintsResponse = sprints.stream().map(s -> {
            SprintResponse response = modelMapper.map(s, SprintResponse.class);
            List<StoryResponse> storyResponses = s.getStories().stream().map(str -> {
                StoryResponse storyResponse = modelMapper.map(str, StoryResponse.class);
                if (str.getEpic() != null) {
                    storyResponse.setEpic(modelMapper.map(str.getEpic(), EpicResponse.class));
                }
                return storyResponse;
            }).toList();
            response.setStories(storyResponses);
            return response;
        }).toList();

        return ResponseEntity.ok(sprintsResponse);
    }

    @PostMapping
    public ResponseEntity<SprintResponse> create(@RequestBody CreateSprintRequest request) {
        Sprint sprint = modelMapper.map(request, Sprint.class);
        Long totalSprintsInProject = sprintRepository.countByProjectId(request.getProjectId());
        sprint.setSequence(totalSprintsInProject.intValue() + 1);
        Sprint newSprint = sprintRepository.save(sprint);
        SprintResponse response = modelMapper.map(newSprint, SprintResponse.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{sprint_id}/start")
    public ResponseEntity<SprintResponse> startSprint(@PathVariable("sprint_id") UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprint not found"));
        sprint.setIsRunning(true);
        sprint.setActualStartDate(new Date());
        Sprint newSprint = sprintRepository.save(sprint);
        SprintResponse response = modelMapper.map(newSprint, SprintResponse.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{sprint_id}/end")
    public ResponseEntity<SprintResponse> endSprint(@PathVariable("sprint_id") UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprint not found"));
        sprint.setIsRunning(false);
        sprint.setActualEndDate(new Date());
        Sprint newSprint = sprintRepository.save(sprint);
        SprintResponse response = modelMapper.map(newSprint, SprintResponse.class);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{project_id}/active")
    public ResponseEntity<SprintResponse> activeSprintByProject(@PathVariable("project_id") UUID projectId) {
        Sprint sprint = sprintRepository.findFirstByProjectIdAndIsRunningIsTrueOrderBySequenceDesc(projectId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprint not found"));
        
        SprintResponse response = modelMapper.map(sprint, SprintResponse.class);
        // List<StoryResponse> storyResponses = modelMapper.map(sprint.getStories(), new TypeToken<List<StoryResponse>>() {}.getType());
        List<StoryResponse> storyResponses = sprint.getStories().stream().map(str -> {
            StoryResponse result = modelMapper.map(str, StoryResponse.class);
            if (str.getAssignee() != null) {
                result.setAssignee(modelMapper.map(str.getAssignee(), UserResponse.class));
            }
            return result;
        }).toList();
        response.setStories(storyResponses);
        
        return ResponseEntity.ok(response);
    }
}
