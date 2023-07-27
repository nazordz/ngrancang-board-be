package com.unindra.ngrancang.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.unindra.ngrancang.dto.requests.CreateIssueRequest;
import com.unindra.ngrancang.enumeration.IssuePriority;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.enumeration.IssueType;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;
import com.unindra.ngrancang.model.Issue;
import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.repository.IssueRepository;
import com.unindra.ngrancang.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class IssueService {
    public static final long MAX_FILE_SIZE = 10L * 1024L * 1024L;
    
    @Value("${env.data.uploadDir}")
    private String uploadDir;
    
    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserDetailsServiceImpl userDetail;

    @Autowired
    private UserRepository userRepository;
    
    public Issue createIssue(CreateIssueRequest request) {
        try {
            Issue issue = new Issue();
            issue.setProjectId(request.getProjectId());
            issue.setUserId(userDetail.currentUser().getId());
            // issue.setType(IssueType.fromCode(request.getType()));
            // issue.setPriority(IssuePriority.fromCode(request.getPriority()));
            // issue.setStatus(IssueStatus.fromCode(request.getStatus()));
            issue.setSummary(request.getSummary());
            issue.setDescription(request.getDescription());

            if (request.getAssignee() != null) {
                User assignee = userRepository.findById(request.getAssignee())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assigneed user not found"));
    
                // issue.setAssignee(assignee);
            }
            if (request.getAttachments() != null && request.getAttachments().length > 0) {
                List<String> uploadedAttachments = new ArrayList<>();
                for (MultipartFile attachment : request.getAttachments()) {

                    if (attachment.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attachment not uploaded");
                    }
            
                    // Check the file content type
                    String contentType = attachment.getContentType();
                    if (contentType != null && !contentType.startsWith("image/")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attachment isn't image");
                    }
            
                    // Check the file size
                    long fileSize = attachment.getSize();
                    if (fileSize > MAX_FILE_SIZE) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File upload failed: File size exceeded");
                    }
                    
                    String fileName = attachment.getOriginalFilename();
                    String filePath = uploadDir + fileName;
                    attachment.transferTo(new File(filePath));
                    uploadedAttachments.add(filePath);
                }
                issue.setAttachments(uploadedAttachments);
            }
            issue.setEpicId(request.getEpicId());
            issue.setStoryPoint(request.getStoryPoint());

            return issueRepository.save(issue);
        } catch (Exception e) {
            log.error("failed to create new issue", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed to create new issue");
        }
    }
}
