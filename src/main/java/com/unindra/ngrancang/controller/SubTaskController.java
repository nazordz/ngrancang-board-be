package com.unindra.ngrancang.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.unindra.ngrancang.dto.requests.CreateSubTasksSimpleRequest;
import com.unindra.ngrancang.dto.requests.ListSubTaskSequenceRequest;
import com.unindra.ngrancang.dto.requests.UpdateSubTasksSequence;
import com.unindra.ngrancang.dto.requests.UpdateSubTasksSimpleRequest;
import com.unindra.ngrancang.dto.responses.SubTaskResponse;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.model.Story;
import com.unindra.ngrancang.model.SubTask;
import com.unindra.ngrancang.repository.StoryRepository;
import com.unindra.ngrancang.repository.SubTaskRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/subtasks")
public class SubTaskController {
    
    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping
    public Page<SubTask> getSubTask(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return subTaskRepository.findAll(pageable);
    }

    @GetMapping("/story/{story_id}")
    public List<SubTask> getSubTasksByStoryId(@PathVariable("story_id") UUID storyId) {
        return subTaskRepository.findByStoryId(storyId);
    }

    @PostMapping("/store-simple")
    public ResponseEntity<SubTaskResponse> storeSimpleSubTask(@RequestBody CreateSubTasksSimpleRequest request) {
        Story selectedStory = storyRepository.findById(request.getStoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "story with id "+request.getStoryId()+" not found"));
        int totalSubTask = subTaskRepository.countByStoryId(request.getStoryId()).intValue() + 1;
        
        SubTask subtask = new SubTask();
        subtask.setId(request.getId());
        subtask.setDescription(request.getDescription());
        subtask.setStoryId(request.getStoryId());
        subtask.setKey(selectedStory.getKey()+"-"+"TASK"+totalSubTask);
        subtask.setStatus(IssueStatus.TODO);
        
        SubTask updatedSubTask = subTaskRepository.save(subtask);
        SubTaskResponse response = modelMapper.map(updatedSubTask, SubTaskResponse.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<SubTaskResponse> updateDescriptionSubTask(@PathVariable("id") UUID id, @RequestBody UpdateSubTasksSimpleRequest request) {
        SubTask subtask = subTaskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub task with id "+id+" not found"));
        subtask.setDescription(request.getDescription());

        SubTask updatedSubTask = subTaskRepository.save(subtask);
        SubTaskResponse response = modelMapper.map(updatedSubTask, SubTaskResponse.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{subtask_id}/assignee")
    public ResponseEntity<SubTaskResponse> updateAssigneeSubTask(
        @PathVariable("subtask_id") UUID subtaskId,
        @RequestBody UpdateSubTasksSimpleRequest request
    ) {
        SubTask subtask = subTaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub task with id "+subtaskId+" not found"));
        subtask.setAssigneeId(request.getAssigneeId());

        SubTask updatedSubTask = subTaskRepository.save(subtask);
        SubTaskResponse response = modelMapper.map(updatedSubTask, SubTaskResponse.class);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{subtask_id}/status")
    public ResponseEntity<SubTaskResponse> updateStatusSubTask(
        @PathVariable("subtask_id") UUID subtaskId,
        @RequestBody UpdateSubTasksSimpleRequest request
    ) {
        SubTask subtask = subTaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub task with id "+subtaskId+" not found"));
        subtask.setStatus(request.getStatus());

        SubTask updatedSubTask = subTaskRepository.save(subtask);
        SubTaskResponse response = modelMapper.map(updatedSubTask, SubTaskResponse.class);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/update-list-subtask")
    public List<SubTask> updateSubtaskSequence(@RequestBody UpdateSubTasksSequence request) throws ResponseStatusException {
        List<ListSubTaskSequenceRequest> subtasks = request.getSubtasks();
        List<SubTask> savedSubtasks = new ArrayList<>();
        subtasks.stream().forEach((sub) -> {
            SubTask st = subTaskRepository.findById(sub.getId()).get();
            st.setSequence(sub.getSequence());
            subTaskRepository.save(st);
            savedSubtasks.add(st);
        });

        return savedSubtasks;
    }
    
}
