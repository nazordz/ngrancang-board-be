package com.unindra.ngrancang.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssuePriority;
import com.unindra.ngrancang.enumeration.IssuePriorityConverter;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.enumeration.IssueType;
import com.unindra.ngrancang.enumeration.IssueTypeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "issues")
@Data
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Issue {
    @Id
    @GeneratedValue(generator = "uuidv4")
    @GenericGenerator(name = "uuidv4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "sprint_id")
    private UUID sprintId;
    
    @ManyToOne(targetEntity = Sprint.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", insertable = false, updatable = false)
    private Sprint sprint;

    @Column(name = "project_id")
    private UUID projectId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @Column(name = "type")
    @Convert(converter = IssueType.Converter.class)
    // @Enumerated(EnumType.STRING)
    private IssueType type;

    @Column(name = "priority")
    @Convert(converter = IssuePriorityConverter.class)
    private IssuePriority priority;
    
    @Column(name = "status")
    private IssueStatus status;

    @Column
    private String summary;

    @Column(columnDefinition = "json")
    private List<String> attachments;

    @Column
    private String description;

    @Column(name = "story_point")
    private Integer storyPoint;

    // @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    // @JoinColumn(name = "assignee")
    // private User assignee;

    @Column(name = "epic_id")
    private UUID epicId;
    
    // @ManyToOne(targetEntity = Issue.class)
    // @JoinColumn(name = "epic_id")
    // private Issue epic;

    // @OneToMany(mappedBy = "epic")
    // private List<Issue> subTasks;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    @UpdateTimestamp
    private Timestamp deletedAt;
}
