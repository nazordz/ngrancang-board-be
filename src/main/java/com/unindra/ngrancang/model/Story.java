package com.unindra.ngrancang.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.enumeration.IssuePriority;
import com.unindra.ngrancang.enumeration.IssuePriorityConverter;
import com.unindra.ngrancang.enumeration.IssueStatus;
import com.unindra.ngrancang.enumeration.IssueStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stories")
@SQLDelete(sql = "UPDATE stories SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // removeable
@NoArgsConstructor
public class Story{
    @Id
    @GeneratedValue(generator = "uuidv4")
    @GenericGenerator(name = "uuidv4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", name = "id")
    private UUID id;

    @Column
    private String key;

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

    @Column(name = "priority", columnDefinition = "issue_priority", nullable = true)
    @Convert(converter = IssuePriorityConverter.class)
    private IssuePriority priority;

    @Column(name = "status", columnDefinition = "issue_status", nullable = true)
    @Convert(converter = IssueStatusConverter.class)
    private IssueStatus status;

    @Column
    private String summary;

    @Column(name = "assignee_id")
    private UUID assigneeId;
    
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", insertable = false, updatable = false)
    private User assignee;

    @Column(columnDefinition = "json")
    private List<String> attachments;

    @Column
    private String description;

    @Column(name = "story_point")
    private Integer storyPoint;

    @Column(name = "epic_id")
    private UUID epicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epic_id", insertable = false, updatable = false)
    private Epic epic;

    @Column(nullable = true)
    private int sequence;

    // @JsonManagedReference
    // @JsonIgnoreProperties("story")
    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY)
    // @JsonIdentityReference(alwaysAsId = false) //removeable
    // @JsonIgnore
    @OrderBy("sequence ASC")
    private List<SubTask> subTasks;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private Timestamp deletedAt;
}
