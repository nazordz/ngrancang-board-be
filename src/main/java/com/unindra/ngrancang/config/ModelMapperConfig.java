package com.unindra.ngrancang.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Condition;
import org.modelmapper.TypeToken;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;

import com.unindra.ngrancang.dto.responses.EpicResponse;
import com.unindra.ngrancang.dto.responses.ProjectResponse;
import com.unindra.ngrancang.dto.responses.SprintResponse;
import com.unindra.ngrancang.dto.responses.StoryResponse;
import com.unindra.ngrancang.dto.responses.SubTaskResponse;
import com.unindra.ngrancang.model.Epic;
import com.unindra.ngrancang.model.Project;
import com.unindra.ngrancang.model.Sprint;
import com.unindra.ngrancang.model.Story;
import com.unindra.ngrancang.model.SubTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ModelMapperConfig {
    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldAccessLevel(AccessLevel.PRIVATE);

        Converter<Epic, EpicResponse> toEpicResponse = context -> {
            Epic source = context.getSource();
            if (source == null) return null;

            return modelMapper.map(source, EpicResponse.class);
        };
        Converter<Story, StoryResponse> toStoryGetEpicResponse = context -> {
            Story source = context.getSource();
            if (source.getEpic() == null) return null;
            return modelMapper.map(source, StoryResponse.class);
        };

        Condition<Story, StoryResponse> isEpicCalled = context -> {
            return context.getSource().getEpic() != null;
        };
        
        // modelMapper.when(isEpicCalled)
        //     .map(src -> src.getEpic(), (dest, value) -> dest.setEpic(modelMapper.map(value, EpicResponse.class)))
        //     .skip();
        Condition<Story, Boolean> isEpicNotNull = context -> context.getSource().getEpic() != null;
        PropertyMap<Story, StoryResponse> storyMap = new PropertyMap<Story, StoryResponse>() {
            
            @Override
            protected void configure() {
                map().setProjectId(source.getProjectId());
                map().setUserId(source.getUserId());
                map().setEpicId(source.getEpicId());
                map().setSprintId(source.getSprintId());
                map().setAssigneeId(source.getAssigneeId());
                // Condition<Story, StoryResponse> isProjectCalled = context -> context.getSource().getProject() != null;
                // when(isProjectCalled).map(Story::getProject, StoryResponse::setProject);
                // if (source.getProject() != null) {
                    // ProjectResponse pr = modelMapper.map(source.getProject(), ProjectResponse.class);
                    // map().setProject(modelMapper.map(source.getProject(), ProjectResponse.class));
                // }
            
                skip().setProject(null);
                skip().setEpic(null);

                // skip().setActiveSprintLogs(new ArrayList<>());
                // when(isEpicNotNull).using(toEpicResponse);

                // map().setUser(null);
                // map().setAssignee(null);

                // Testing
                // Condition<Story, StoryResponse> isSubTaskNotEmpty = context -> !context.getSource().getSubTasks().isEmpty();
                // List<SubTaskResponse> subtasks = source.getSubTasks().stream().map(st -> {
                //     return modelMapper.map(st, SubTaskResponse.class);
                // }).toList();

                // Type subTaskType = new TypeToken<List<SubTaskResponse>>() {}.getType();
                // List<SubTaskResponse> strr = modelMapper.map(source.getSubTasks(), subTaskType);
                
                // map().setSubTasks(strr);
                // when(isSubTaskNotEmpty).map(source.getSubTasks(), subTaskType);
            }
        };
        modelMapper.addMappings(storyMap);
        

       PropertyMap<SubTask, SubTaskResponse> subTaskMap = new PropertyMap<SubTask, SubTaskResponse>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUserId());
                map().setAssigneeId(source.getAssigneeId());
                map().setStoryId(source.getStoryId());
                map().setAssignee(null);
                map().setUser(null);
                map().setStory(null);
            }
        };
        modelMapper.addMappings(subTaskMap);
        
        // For mapping List<?>
        modelMapper.getConfiguration().setPropertyCondition(new Condition<Object, Object>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return !(context.getSource() instanceof PersistentCollection);
            }
        });

        Converter<List<SubTask>, List<SubTaskResponse>> convertSubTasks = new AbstractConverter<>(){
            protected List<SubTaskResponse> convert(List<SubTask> source) {
                return source.stream().map(st -> {
                    SubTaskResponse str = modelMapper.map(st, SubTaskResponse.class);
                    return str;
                }).toList();
            };
        };

        modelMapper.typeMap(Story.class, StoryResponse.class)
            .addMappings(mapper -> {
                mapper.using(convertSubTasks)
                        .map(Story::getSubTasks, StoryResponse::setSubTasks);
                // mapper.when(isEpicCalled).using(toEpicResponse).map(Story::getEpic, StoryResponse::setEpic);
            });

        modelMapper.typeMap(Project.class, ProjectResponse.class)
                    .addMappings(mapper -> {
                        mapper.map(Project::getId, ProjectResponse::setId);
                        mapper.<String>map(Project::getUserId, ProjectResponse::setUserId);
                    });

        PropertyMap<Sprint, SprintResponse> sprintMap = new PropertyMap<Sprint,SprintResponse>() {
            @Override
            protected void configure() {
                map().setProject(null);

                Condition<Sprint, SprintResponse> storiesNotEmpty = context -> !context.getSource().getStories().isEmpty();

            }
        };
        modelMapper.addMappings(sprintMap);
        
        modelMapper.typeMap(Epic.class, EpicResponse.class)
                    .addMappings(mapper -> {
                        mapper.map(Epic::getProjectId, EpicResponse::setProjectId);
                        mapper.map(Epic::getUserId, EpicResponse::setUserId);
                    });
        PropertyMap<Epic, EpicResponse> propertyMap = new PropertyMap<Epic,EpicResponse>() {
            @Override
            protected void configure() {
                map().setProject(null);
                map().setUser(null);
            }
        };

        modelMapper.addMappings(propertyMap);
        
        return modelMapper;
    }
}
