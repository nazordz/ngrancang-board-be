package com.unindra.ngrancang.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.unindra.ngrancang.dto.requests.CreateIssueRequest;
import com.unindra.ngrancang.enumeration.IssueType;
import com.unindra.ngrancang.model.Issue;
import com.unindra.ngrancang.repository.IssueRepository;
import com.unindra.ngrancang.services.IssueService;


@RestController
@RequestMapping(path = "/api/issues")
public class IssueController {
    
    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueService issueService;
    
    @GetMapping
    public Page<Issue> getIssues(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return issueRepository.findByTypeNot(IssueType.EPIC, pageable);
        // return issueRepository.findByTypeNot(IssueType.EPIC.getCode(), page, size);
    }

    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute CreateIssueRequest request) {
        try {
            Issue issue = issueService.createIssue(request);
            return ResponseEntity.ok(issue);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
        }
    }
}
