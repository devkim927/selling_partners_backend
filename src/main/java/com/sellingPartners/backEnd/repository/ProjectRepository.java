package com.sellingPartners.backEnd.repository;

import com.sellingPartners.backEnd.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sellingPartners.backEnd.entity.UserEntity;
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
	Page<ProjectEntity> findAllByUser(UserEntity creator, Pageable pageable);
}
