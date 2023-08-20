package com.unindra.ngrancang.model;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "active_sprint_logs")
@Data
public class ActiveSprintLog {
    @Id
    @GeneratedValue(generator = "uuidv4")
    @GenericGenerator(name = "uuidv4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", name = "id")
    private UUID id;

    @Column(name = "story_id")
    private UUID storyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", insertable = false, updatable = false)
    private Story story;

    @Column(name = "sprint_id")
    private UUID sprintId;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "sprint_id", insertable = false, updatable = false)
    private Sprint sprint;

    @Column(name = "story_point")
    private Integer storyPoint;

    @Column
    @Convert(converter = IssueStatusConverter.class)
    private IssueStatus status;

    @Column(name = "is_start")
    private boolean isStart;
    
    @Column(name = "is_end")
    private boolean isEnd;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
    
}
