package com.unindra.ngrancang.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.ngrancang.dto.requests.CreateEpicsRequest;
import com.unindra.ngrancang.dto.responses.EpicResponse;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;
import com.unindra.ngrancang.model.Epic;
import com.unindra.ngrancang.repository.EpicRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/epics")
public class EpicController {
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private EpicRepository epicRepository;

    @Autowired
    private UserDetailsServiceImpl userDetail;
    
    @GetMapping("/paginate")
    public Page<Epic> epicPagination(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return epicRepository.findAll(pageable);
    }

    @GetMapping("/project/{project_id}")
    public List<EpicResponse> getByProjectId(@PathVariable("project_id") UUID projectId) {
        List<Epic> findByProjectId = epicRepository.findByProjectId(projectId);
        Type EpicResponseToken = new TypeToken<List<EpicResponse>>() {}.getType();
        List<EpicResponse> epicResponses = modelMapper.map(findByProjectId, EpicResponseToken);
        return epicResponses;
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<EpicResponse>> storeEpics(@RequestBody CreateEpicsRequest request) {
        List<Epic> epics = modelMapper.map(request.getEpics(), new TypeToken<List<Epic>>() {}.getType());
        epics = epics.stream().map(ep -> {
            ep.setUserId(userDetail.currentUser().getId());
            return ep;
        }).toList();
        List<Epic> newEpics = epicRepository.saveAll(epics);
        List<EpicResponse> response = modelMapper.map(newEpics, new TypeToken<List<EpicResponse>>() {}.getType());
        return ResponseEntity.ok(response);
    }
}
