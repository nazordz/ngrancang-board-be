package com.unindra.ngrancang.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unindra.ngrancang.model.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
