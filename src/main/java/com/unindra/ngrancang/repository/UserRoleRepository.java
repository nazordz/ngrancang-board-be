package com.unindra.ngrancang.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unindra.ngrancang.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID>{
    
}
