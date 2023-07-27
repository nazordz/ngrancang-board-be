package com.unindra.ngrancang.controller;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Pageable;

import com.unindra.ngrancang.dto.requests.ProjectRequest;
import com.unindra.ngrancang.dto.responses.MessageResponse;
import com.unindra.ngrancang.dto.responses.ProjectResponse;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;
import com.unindra.ngrancang.model.Project;
import com.unindra.ngrancang.repository.ProjectRepository;
import com.unindra.ngrancang.services.FilesStorageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/projects")
@Slf4j
public class ProjectController {

    @Value("${env.data.uploadDir}")
    private String uploadDir;
    
    @Autowired
    FilesStorageService storageService;
    
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired 
    private ModelMapper modelMapper;
    

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> responses = modelMapper.map(projects, new TypeToken<List<ProjectResponse>>(){}.getType());
        return responses;
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable("id") UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return modelMapper.map(project, ProjectResponse.class);
    }

    @PostMapping
    public ProjectResponse createProject(@ModelAttribute ProjectRequest projectRequest) {
        Project project = new Project();
        project.setKey(projectRequest.getKey());
        project.setName(projectRequest.getName());
        if (projectRequest.getAvatar() != null && !projectRequest.getAvatar().isEmpty()) {
            try {
                // String fileName = projectRequest.getAvatar().getOriginalFilename();
                String fileName = storageService.save(projectRequest.getAvatar());
                project.setAvatar("assets/"+fileName);
            } catch (RuntimeException e) {
                // e.printStackTrace(System.out);
                log.error("Failed to upload avatar", e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed to upload avatar");
            }
        } else {
            project.setAvatar(null);

        }
        project.setUser(userService.currentUser());
        project.setDescription(projectRequest.getDescription());
        Project savedProject = projectRepository.save(project);
        return modelMapper.map(savedProject, ProjectResponse.class);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable UUID id, @ModelAttribute ProjectRequest projectRequest) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        
        project.setKey(projectRequest.getKey());
        project.setName(projectRequest.getName());
        if (!projectRequest.getAvatar().isEmpty()) {
            try {
                // String fileName = projectRequest.getAvatar().getOriginalFilename();
                String fileName = storageService.save(projectRequest.getAvatar());

                project.setAvatar("assets/"+fileName);
            } catch (RuntimeException e) {
                log.error("Failed to upload avatar", e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File failed to upload");
            }
        }
        project.setDescription(projectRequest.getDescription());
        Project updatedProject = projectRepository.save(project);
        return modelMapper.map(updatedProject, ProjectResponse.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable UUID id) {
        projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        projectRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Project has been deleted"));
    }
    
    @GetMapping("/paginate")
    public Page<ProjectResponse> paginate(
        @RequestParam(defaultValue = "0", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "size") int size,
        @RequestParam(defaultValue = "", name = "search") String search
    ) {
        Pageable pageable = PageRequest.of(page, size);

        if (!search.isBlank()) {
            Page<Project> findAll = projectRepository.findByNameContainingIgnoreCase(search, pageable);
            return findAll.map(project -> modelMapper.map(project, ProjectResponse.class));
        } else {
            Page<Project> findAll = projectRepository.findAll(pageable);
            return findAll.map(project -> modelMapper.map(project, ProjectResponse.class));
        }
    }
}
