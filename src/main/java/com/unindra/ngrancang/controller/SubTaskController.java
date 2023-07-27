package com.unindra.ngrancang.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    public SubTask storeSimpleSubTask(@RequestBody CreateSubTasksSimpleRequest request) {
        Story selectedStory = storyRepository.findById(request.getStoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "story with id "+request.getStoryId()+" not found"));
        int totalSubTask = subTaskRepository.countByStoryId(request.getStoryId()).intValue() + 1;
        
        SubTask subtask = new SubTask();
        subtask.setId(request.getId());
        subtask.setDescription(request.getDescription());
        subtask.setStoryId(request.getStoryId());
        subtask.setKey(selectedStory.getKey()+"-"+"TASK"+totalSubTask);
        subtask.setStatus(IssueStatus.TODO);
        
        return subTaskRepository.save(subtask);
    }

    @PatchMapping("/update-description/{id}")
    public SubTask updateDescriptionSubTask(@PathVariable("id") UUID id, @RequestBody UpdateSubTasksSimpleRequest request) {
        SubTask subtask = subTaskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub task with id "+id+" not found"));
        subtask.setDescription(request.getDescription());

        return subTaskRepository.save(subtask);
    }

    @PatchMapping("/update-assignee/{subtask_id}")
    public SubTask updateAssigneeSubTask(
        @PathVariable("subtask_id") UUID subtaskId,
        @RequestBody UpdateSubTasksSimpleRequest request
    ) {
        SubTask subtask = subTaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub task with id "+subtaskId+" not found"));
        subtask.setAssigneeId(request.getAssigneeId());

        return subTaskRepository.save(subtask);
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
