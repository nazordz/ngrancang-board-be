package com.unindra.ngrancang.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unindra.ngrancang.model.Epic;
import java.util.List;


public interface EpicRepository extends JpaRepository<Epic, UUID>{
    List<Epic> findByProjectId(UUID projectId);
}
