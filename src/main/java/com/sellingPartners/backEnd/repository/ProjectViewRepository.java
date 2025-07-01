package com.sellingPartners.backEnd.repository;

import com.sellingPartners.backEnd.entity.ProjectViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProjectViewRepository extends JpaRepository<ProjectViewEntity, Long> {
    boolean existsByUserIdAndProjectId(Long userId, Long projectId);
}