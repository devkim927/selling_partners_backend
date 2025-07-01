package com.sellingPartners.backEnd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sellingPartners.backEnd.entity.PartnerProfileEntity;

@Repository
public interface PartnerProfileRepository extends JpaRepository<PartnerProfileEntity, Long> {
	Page<PartnerProfileEntity> findAll(Pageable pageable);
}
