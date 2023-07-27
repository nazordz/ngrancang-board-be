package com.unindra.ngrancang.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.unindra.ngrancang.dto.requests.CreateStoryRequest;
import com.unindra.ngrancang.dto.requests.ListStorySequenceRequest;
import com.unindra.ngrancang.dto.requests.UpdateStoryRequest;
import com.unindra.ngrancang.dto.requests.UpdateStorySequenceInActiveSprintRequest;
import com.unindra.ngrancang.dto.requests.UpdateStorySequenceRequest;
import com.unindra.ngrancang.dto.responses.MessageResponse;
import com.unindra.ngrancang.dto.responses.StoryResponse;
import com.unindra.ngrancang.dto.responses.SubTaskResponse;
import com.unindra.ngrancang.dto.responses.UserResponse;
import com.unindra.ngrancang.enumeration.IssuePriority;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;
import com.unindra.ngrancang.model.Story;
import com.unindra.ngrancang.repository.ProjectRepository;
import com.unindra.ngrancang.repository.StoryRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/stories")
public class StoryController {
    
    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping
    public Page<Story> storyPaginate(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return storyRepository.findAll(pageable);
    }

    @GetMapping(value = "/project/{project_id}")
    public ResponseEntity<List<StoryResponse>> storyByProject(
        @PathVariable("project_id") UUID projectId,
        @RequestParam(value = "sprint_id", required = false) UUID sprintId,
        @RequestParam(value = "epic_id", required = false) UUID epicId,
        @RequestParam(value = "show_backlog_only", required = false) boolean showBacklogOnly
    ) {
        List<Story> stories = new ArrayList<>();
        if (sprintId != null) {
            stories = storyRepository.findByProjectIdAndSprintIdOrderBySequenceAsc(projectId, sprintId);
        } else if (epicId != null) {
            stories = storyRepository.findByProjectIdAndEpicIdOrderBySequenceAsc(projectId, epicId);
        } else if (showBacklogOnly) {
            stories = storyRepository.findByProjectIdAndSprintIdIsNullOrderBySequenceAsc(projectId);
        } else {
            stories = storyRepository.findByProjectIdOrderBySequenceAsc(projectId);
        }
        // List<StoryResponse> storiesResponses = stories.stream().map(story -> {
        //     StoryResponse res = modelMapper.map(story, StoryResponse.class);
        //     res.setSubTasks(
        //         story.getSubTasks().stream().map(st -> modelMapper.map(st, SubTaskResponse.class)).toList()
        //     );
        //     return res;
        // }).toList();
        List<StoryResponse> storiesResponses = stories.stream().map(story -> {
            StoryResponse res = modelMapper.map(story, StoryResponse.class);
            return res;
        }).toList();
        return ResponseEntity.ok(storiesResponses);
    }

    @PostMapping("/simple-story")
    @Transactional
    public Story createSimpleStory(@RequestBody CreateStoryRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        String key = projectRepository.findById(request.getProjectId()).get().getKey();
        int countStories = storyRepository.countAllIncludingDeletedByProjectId(request.getProjectId()).intValue() + 1;
        int currentSequence = storyRepository.countByProjectIdAndSprintIdIsNull(request.getProjectId()).intValue() + 1;
        Story story = new Story();
        story.setId(request.getId());
        story.setSummary(request.getSummary());
        story.setProjectId(request.getProjectId());
        story.setUserId(userService.currentUser().getId());
        story.setPriority(IssuePriority.MEDIUM);
        story.setStatus(IssueStatus.TODO);
        story.setKey(key + "-"+countStories);
        story.setStoryPoint(0);
        story.setSequence(currentSequence);
        story.setCreatedAt(timestamp);
        story.setUpdatedAt(timestamp);

        return storyRepository.save(story);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryResponse> findById(@PathVariable("id") UUID id) {
        Story selectedStory = storyRepository.findWithUserAndSprintAndAssigneeAndEpicAndSubTasksById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "story not found"));
        StoryResponse response = modelMapper.map(selectedStory, StoryResponse.class);
        // Another way
        // StoryResponse response = modelMapper.map(selectedStory, StoryResponse.class);

        response.setSubTasks(
            selectedStory.getSubTasks().stream().map(st -> {
                SubTaskResponse str = modelMapper.map(st, SubTaskResponse.class);
                if (st.getAssignee() != null) {
                    str.setAssignee(modelMapper.map(st.getAssignee(), UserResponse.class));
                }
                return str;
            }).toList()
        );
        
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public StoryResponse update(@PathVariable("id") UUID id, @RequestBody UpdateStoryRequest request) {
        Story story = storyRepository.findById(id)  
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "story with id "+id+" not found"));
        modelMapper.map(request, story);
        // Story updateData = modelMapper.map(request, Story.class);
        // updateData.setId(story.getId());
        // story.setAssigneeId(request.getAssigneeId());
        // story.setDescription(request.getDescription());
        // story.setEpicId(request.getEpicId());
        // story.setPriority(request.getPriority());
        // story.setSprintId(request.getSprint());
        // story.setStatus(request.getStatus());
        // story.setSummary(request.getSummary());
        // story.setStoryPoint(request.getStoryPoint());
        Story newStory = storyRepository.save(story);
        StoryResponse response = modelMapper.map(newStory, StoryResponse.class);
        return response;
    }

    @PutMapping("/update-list-story")
    public List<StoryResponse> updateSequenceStories(
        @RequestBody UpdateStorySequenceRequest request
    ) throws ResponseStatusException {
        List<ListStorySequenceRequest> listDraftStory = request.getStories();
        List<StoryResponse> newStories = new ArrayList<>();
        listDraftStory.stream().forEach(draftStory -> {
            Story str = storyRepository.findById(draftStory.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "story with id "+draftStory.getId()+" not found"));
                str.setSequence(draftStory.getSequence());
                str.setSprintId(request.getSprintId());
                storyRepository.save(str);
            newStories.add(modelMapper.map(str, StoryResponse.class));
        });
        return newStories;
    }
    @PutMapping("/update-list-story-active-sprint")
    public List<StoryResponse> updateSequenceStoriesInSprint(
        @RequestBody UpdateStorySequenceInActiveSprintRequest request
    ) throws ResponseStatusException {
        List<StoryResponse> listDraftStory = request.getStories();
        List<Story> updateStories = new ArrayList<>();
        
        listDraftStory.stream().forEach(draftStory -> {
            Story str = storyRepository.findById(draftStory.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "story with id "+draftStory.getId()+" not found"));
                str.setSprintId(request.getSprintId());
                str.setStatus(draftStory.getStatus());
                str.setSequence(draftStory.getSequence());
                updateStories.add(str);
        });
        List<Story> newStories = storyRepository.saveAll(updateStories);
        List<StoryResponse> responses = modelMapper.map(newStories, new TypeToken<List<StoryResponse>>(){}.getType());
        return responses;
    }

    @DeleteMapping("/{story_id}")
    public ResponseEntity<MessageResponse> deleteById(@PathVariable("story_id") UUID storyId) {
        storyRepository.deleteById(storyId);
        MessageResponse response = new MessageResponse("Story has been deleted");
        return ResponseEntity.ok(response);
    }
}
