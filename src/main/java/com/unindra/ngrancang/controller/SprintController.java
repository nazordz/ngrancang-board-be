package com.unindra.ngrancang.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.unindra.ngrancang.dto.requests.CreateSprintRequest;
import com.unindra.ngrancang.dto.responses.EpicResponse;
import com.unindra.ngrancang.dto.responses.SprintResponse;
import com.unindra.ngrancang.dto.responses.StoryResponse;
import com.unindra.ngrancang.dto.responses.UserResponse;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.model.ActiveSprintLog;
import com.unindra.ngrancang.model.Sprint;
import com.unindra.ngrancang.model.Story;
import com.unindra.ngrancang.repository.ActiveSprintLogRepository;
import com.unindra.ngrancang.repository.SprintRepository;
import com.unindra.ngrancang.repository.StoryRepository;
import com.unindra.ngrancang.services.SprintService;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {
    
    @Autowired
    private SprintService sprintService;
    
    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ActiveSprintLogRepository activeSprintLogRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("{sprint_id}")
    public ResponseEntity<SprintResponse> findById(@PathVariable("sprint_id") UUID sprintId) {

        Optional<SprintResponse> response = sprintService.findySprintById(sprintId);
        if (response.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprint not found");
        }
        return ResponseEntity.ok(response.get());
        
    }

    @GetMapping("/project/{project_id}")
    public ResponseEntity<List<SprintResponse>> findSprintByProject(
        @PathVariable("project_id") UUID projectId,
        @RequestParam(name = "has_ended", defaultValue = "") String hasEnded
    ) {
        List<Sprint> sprints = new ArrayList<Sprint>();

        if (hasEnded.isEmpty()) {
            sprints = sprintRepository.findByProjectId(projectId);
        } else {
            sprints = sprintRepository.findByProjectIdAndIsRunningFalseAndActualEndDateIsNotNullOrderBySequenceAsc(projectId);
        }
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
        sprint.setPlanStoryPoint(0);
        sprint.setActualStoryPoint(0);
        Sprint newSprint = sprintRepository.save(sprint);
        SprintResponse response = modelMapper.map(newSprint, SprintResponse.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{sprint_id}/start")
    public ResponseEntity<SprintResponse> startSprint(@PathVariable("sprint_id") UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprint not found"));
        sprint.setIsRunning(true);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        sprint.setActualStartDate(timestamp);
        int sumStories = sprint.getStories().stream().mapToInt(str -> str.getStoryPoint()).sum();
        sprint.setPlanStoryPoint(sumStories);
        Sprint newSprint = sprintRepository.save(sprint);
        List<ActiveSprintLog> sprintLogs = new ArrayList<ActiveSprintLog>();
        sprint.getStories().forEach(story -> {
            ActiveSprintLog sprintLog = new ActiveSprintLog();
            sprintLog.setSprintId(sprintId);
            sprintLog.setStoryId(story.getId());
            sprintLog.setStatus(story.getStatus());
            sprintLog.setStoryPoint(story.getStoryPoint());
            sprintLog.setStart(true);
            sprintLogs.add(sprintLog);
        });
        
        activeSprintLogRepository.saveAll(sprintLogs);
        SprintResponse response = modelMapper.map(newSprint, SprintResponse.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{sprint_id}/end")
    public ResponseEntity<SprintResponse> endSprint(@PathVariable("sprint_id") UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprint not found"));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        sprint.setIsRunning(false);
        sprint.setActualEndDate(timestamp);
        int sumStories = sprint.getStories().stream().mapToInt(Story::getStoryPoint).sum();
        sprint.setActualStoryPoint(sumStories);
        Sprint newSprint = sprintRepository.save(sprint);
        List<Story> stories = new ArrayList<Story>();
        newSprint.getStories().forEach(story -> {
            if (story.getStatus() != IssueStatus.DONE) {
                story.setSprintId(null);
                stories.add(story);
            }
        });
        storyRepository.saveAll(stories);
        SprintResponse response = modelMapper.map(newSprint, SprintResponse.class);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{project_id}/active")
    public ResponseEntity<SprintResponse> activeSprintByProject(@PathVariable("project_id") UUID projectId) {
        Sprint sprint = sprintRepository.findFirstByProjectIdAndIsRunningIsTrueOrderBySequenceDesc(projectId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprint not found"));
        
        SprintResponse response = modelMapper.map(sprint, SprintResponse.class);
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

    @GetMapping("/project/{project_id}/velocity-chart")
    public ResponseEntity<List<SprintResponse>> getVelocityChart(@PathVariable("project_id") UUID projectId) {
        List<Sprint> sprints = sprintRepository.findByProjectIdAndIsRunningFalseAndActualEndDateIsNotNullOrderBySequenceAsc(projectId);
        List<SprintResponse> sprintsResponse = sprints.stream().map(s -> {
            return modelMapper.map(s, SprintResponse.class);
        }).toList();

        return ResponseEntity.ok(sprintsResponse);
    }
}
