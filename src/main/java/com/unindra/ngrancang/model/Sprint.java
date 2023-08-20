package com.unindra.ngrancang.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.modelmapper.internal.bytebuddy.dynamic.TypeResolutionStrategy.Active;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sprints")
@Data
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Sprint {
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

    @Column(name = "project_id")
    private UUID projectId;

    @ManyToOne(targetEntity = Project.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable=false, updatable=false)
    private Project project;

    @Column(name = "sprint_name")
    private String sprintName;

    @Column(name = "sprint_goal")
    private String sprintGoal;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @OneToMany(mappedBy = "sprint", targetEntity = Story.class, fetch = FetchType.LAZY)
    @OrderBy("sequence asc")
    private List<Story> stories;

    @OneToMany(mappedBy = "sprint")
    private List<ActiveSprintLog> activeSprintLogs;

    @Column(nullable = false)
    private int sequence;
    
    @Column(name = "plan_story_point")
    private Integer planStoryPoint;
    
    @Column(name = "actual_story_point")
    private Integer actualStoryPoint;

    @Column(nullable = false, name = "is_running")
    private boolean isRunning;

    public boolean getIsRunning() {
        return isRunning;
    }
    public void setIsRunning(boolean value) {
        this.isRunning = value;
    }
    
    @Column(name = "actual_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp actualStartDate;

    @Column(name = "actual_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp actualEndDate;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    @Nullable
    private Timestamp deletedAt;
}
