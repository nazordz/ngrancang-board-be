package com.unindra.ngrancang.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unindra.ngrancang.dto.responses.ActiveSprintLogResponse;
import com.unindra.ngrancang.dto.responses.EpicResponse;
import com.unindra.ngrancang.dto.responses.SprintResponse;
import com.unindra.ngrancang.dto.responses.StoryResponse;
import com.unindra.ngrancang.model.Sprint;
import com.unindra.ngrancang.model.Story;
import com.unindra.ngrancang.repository.SprintRepository;

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Optional<Sprint> getSprintById(UUID sprintId) {
        Optional<Sprint> sprint = sprintRepository.findByIdWithStories(sprintId);
        if (sprint.isPresent()) {
            Sprint currentSprint = sprint.get();


            // SprintResponse sprintResponse = modelMapper.map(currentSprint, SprintResponse.class);
            // List<Story> storyList = new ArrayList<>();
            
            // for (Story story : currentSprint.getStories()) {
            //     // StoryResponse storyResponse = modelMapper.map(story, StoryResponse.class);
                
            //     // Handle other properties and nested entities
            //     // For example:
            //     // storyResponse.setActiveSprintLogs(mapActiveSprintLogs(story.getActiveSprintLogs()));
                
            //     // storyResponses.add(storyResponse);
            //     storyList.add(story);
            // }
            
            // currentSprint.setStories(storyList);
            // Optional<Sprint> mappedSprint = Optional.ofNullable(currentSprint);
            // return mappedSprint;

            
            // sprintResponse.setStories(storyResponses);
            
            
            
            // List<Story> stories = new ArrayList<>(currentSprint.getStories());
            // currentSprint.setStories(new ArrayList<>());
            
            // List<Story> mappedStories = stories.stream().map(str -> {
            //     // str.getActiveSprintLogs().size();
            //     return str;
            // }).toList();
            // currentSprint.setStories(mappedStories);
            // Optional<Sprint> mappedSprint = Optional.ofNullable(currentSprint);
            // return mappedSprint;
        }
        return sprint;
    }


    public Optional<SprintResponse> findySprintById(UUID sprintId) {
        Optional<Sprint> sprint = sprintRepository.findByIdWithStories(sprintId);
        
        return sprint.map(currentSprint -> {
            SprintResponse sprintResponse = modelMapper.map(currentSprint, SprintResponse.class);
            List<StoryResponse> storyResponses = new ArrayList<>();
            
            for (Story story : currentSprint.getStories()) {
                StoryResponse storyResponse = modelMapper.map(story, StoryResponse.class);
                
                // Handle other properties and nested entities
                // For example:
                storyResponse.setActiveSprintLogs(modelMapper.map(story.getActiveSprintLogs(), new TypeToken<List<ActiveSprintLogResponse>>() {}.getType()));
                if (story.getEpic() != null) {
                    storyResponse.setEpic(modelMapper.map(story.getEpic(), EpicResponse.class));
                }
                storyResponses.add(storyResponse);
            }
            
            sprintResponse.setStories(storyResponses);
            
            return sprintResponse;
        });
    }
    
}
