package com.unindra.ngrancang.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unindra.ngrancang.enumeration.IssueType;
import com.unindra.ngrancang.model.Issue;

public interface IssueRepository extends JpaRepository<Issue, UUID>{
    // Page<Issue> findByTypeNot(String type, Pageable pageable);

    // @Query(value = "SELECT * FROM issues WHERE (deleted_at IS NULL) AND (type != ?1) ORDER BY created_at ASC OFFSET ?2 ROWS FETCH FIRST ?3 ROWS ONLY", nativeQuery = true)
    // List<Issue> findByTypeNot(String type, int offset, int limit);

    Page<Issue> findByTypeNot(IssueType type, Pageable pageable);
}
