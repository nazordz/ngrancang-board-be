package com.unindra.ngrancang.repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unindra.ngrancang.model.Story;

public interface StoryRepository extends JpaRepository<Story, UUID>{
    @EntityGraph(attributePaths = {"user", "sprint", "assignee", "epic"})
    @Query("SELECT s FROM Story s WHERE s.projectId = ?1 ORDER BY s.sequence")
    List<Story> findByProjectIdOrderBySequenceAsc(UUID projectId);

    @EntityGraph(attributePaths = {"user", "assignee", "epic"})
    List<Story> findByProjectIdAndSprintIdIsNullOrderBySequenceAsc(UUID projectId);

    @EntityGraph(attributePaths = {"user", "sprint", "assignee", "epic"})
    @Query("SELECT s FROM Story s WHERE s.projectId = ?1 AND s.sprintId = ?2 ORDER BY s.sequence")
    List<Story> findByProjectIdAndSprintIdOrderBySequenceAsc(UUID projectId, UUID sprintId);

    @EntityGraph(attributePaths = {"user", "sprint", "assignee", "epic"})
    @Query("SELECT s FROM Story s WHERE s.projectId = ?1 AND s.epicId = ?2 ORDER BY s.sequence")
    List<Story> findByProjectIdAndEpicIdOrderBySequenceAsc(UUID projectId, UUID epicId);

    Long countByProjectId(UUID projectId);

    Long countByProjectIdAndSprintIdIsNull(UUID projectId);

    @Query(value = "SELECT COUNT(s.*) FROM stories s WHERE s.project_id = :projectId", nativeQuery = true)
    Long countAllIncludingDeletedByProjectId(@Param("projectId") UUID projectId);
    
    List<Story> findByProjectIdInOrderBySequenceAsc(List<UUID> projectIds);

    @Query("SELECT s FROM Story s WHERE s.id = ?1")
    @EntityGraph(attributePaths = {"user", "sprint", "assignee",  "subTasks.assignee"})
    Optional<Story> findWithUserAndSprintAndAssigneeAndEpicAndSubTasksById(UUID id);
    
    @EntityGraph(attributePaths = {"subTasks"})
    Optional<Story> findWithSubTasksById(UUID id);
    
}
