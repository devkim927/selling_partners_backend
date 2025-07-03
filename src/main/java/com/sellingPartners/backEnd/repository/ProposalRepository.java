package com.sellingPartners.backEnd.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sellingPartners.backEnd.entity.ProposalEntity;

@Repository

public interface ProposalRepository extends JpaRepository<ProposalEntity, Long> {

    Page<ProposalEntity> findByToProjectId(Long projectId, Pageable pageable);

    Page<ProposalEntity> findByToProfileId(Long profileId, Pageable pageable);
}
