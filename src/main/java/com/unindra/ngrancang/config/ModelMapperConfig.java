package com.unindra.ngrancang.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


        
        PropertyMap<Story, StoryResponse> storyMap = new PropertyMap<Story, StoryResponse>() {
            
            @Override
            protected void configure() {
                map().setProjectId(source.getProjectId());
                map().setUserId(source.getUserId());
                map().setEpicId(source.getEpicId());
                map().setSprintId(source.getSprintId());
                map().setAssigneeId(source.getAssigneeId());
            
                skip().setProject(null);
                skip().setEpic(null);

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
