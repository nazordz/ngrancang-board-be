package com.unindra.ngrancang.repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unindra.ngrancang.model.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, UUID>{
    public List<Sprint> findByProjectId(UUID projectId);

    @Query("SELECT s FROM Sprint s WHERE s.projectId = ?1 ORDER BY s.sequence ASC")
    @EntityGraph(attributePaths = {"stories.epic"})
    public List<Sprint> findByProjectIdWithStoryOrderBySequenceAsc(UUID projectId);

    @Query("SELECT s FROM Sprint s LEFT JOIN FETCH s.stories stories LEFT JOIN FETCH stories.epic WHERE s.projectId = ?1 ORDER BY s.sequence ASC, stories.sequence ASC")
    public List<Sprint> findByProjectIdWithStoriesOrderBySequenceAsc(UUID projectId);

    Long countByProjectId(UUID projectId);

    // @EntityGraph(attributePaths = {"stories.assignee"})
    public Optional<Sprint> findFirstByProjectIdAndIsRunningIsTrueOrderBySequenceDesc(UUID projectId);
}
