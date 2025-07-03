package com.sellingPartners.backEnd.repository;

import com.sellingPartners.backEnd.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sellingPartners.backEnd.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
	// 전체 조회 쿼리 최적화 적용
	@Query(
	  value = "SELECT p FROM ProjectEntity p JOIN FETCH p.user",
	  countQuery = "SELECT COUNT(p) FROM ProjectEntity p"
	)
	Page<ProjectEntity> findAllWithUser(Pageable pageable);
	// 본인 작성 게시물 조회 쿼리 최적화 적용
	@Query(
	  value = "SELECT p FROM ProjectEntity p JOIN FETCH p.user WHERE p.user = :user",
	  countQuery = "SELECT COUNT(p) FROM ProjectEntity p WHERE p.user = :user"
	)
	Page<ProjectEntity> findAllByUserWithFetch(@Param("user") UserEntity user, Pageable pageable);
}
