package com.unindra.ngrancang.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unindra.ngrancang.model.SubTask;

public interface SubTaskRepository extends JpaRepository<SubTask, UUID> {
    List<SubTask> findByStoryId(UUID storyId);
    Long countByStoryId(UUID storyid);
}
