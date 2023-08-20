package com.unindra.ngrancang.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unindra.ngrancang.model.ActiveSprintLog;

public interface ActiveSprintLogRepository extends JpaRepository<ActiveSprintLog, UUID> {
    
}
