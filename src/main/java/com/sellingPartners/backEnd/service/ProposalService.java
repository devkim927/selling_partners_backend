package com.sellingPartners.backEnd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sellingPartners.backEnd.dto.ProposalRequest;
import com.sellingPartners.backEnd.dto.ProposalResponse;
import com.sellingPartners.backEnd.entity.PartnerProfileEntity;
import com.sellingPartners.backEnd.entity.ProjectEntity;
import com.sellingPartners.backEnd.entity.ProposalEntity;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.repository.PartnerProfileRepository;
import com.sellingPartners.backEnd.repository.ProjectRepository;
import com.sellingPartners.backEnd.repository.ProposalRepository;
import com.sellingPartners.backEnd.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final UserRepository userRepository;
    private final PartnerProfileRepository partnerProfileRepository;
    private final ProjectRepository projectRepository;
    private final ProposalRepository proposalRepository;

    // 제안 생성
    public Long createProposal(Long fromUserId, ProposalRequest dto) {
        if (!dto.isValid()) {
            throw new IllegalArgumentException("toProfileId 또는 toProjectId 중 하나만 있어야 합니다.");
        }

        UserEntity fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("제안자(User)를 찾을 수 없습니다."));

        PartnerProfileEntity toProfile = null;
        ProjectEntity toProject = null;

        if (dto.getToProfileId() != null) {
            toProfile = partnerProfileRepository.findById(dto.getToProfileId())
                    .orElseThrow(() -> new RuntimeException("파트너 프로필을 찾을 수 없습니다."));
        } else if (dto.getToProjectId() != null) {
            toProject = projectRepository.findById(dto.getToProjectId())
                    .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));
        }

        ProposalEntity proposal = ProposalEntity.builder()
                .fromUser(fromUser)
                .toProfile(toProfile)
                .toProject(toProject)
                .message(dto.getMessage())
                .proposedPrice(dto.getProposedPrice())
                .createdAt(LocalDateTime.now())
                .build();

        return proposalRepository.save(proposal).getId();
    }

    // 프로젝트에 달린 제안 리스트 조회
    public List<ProposalResponse> getProposalsByProject(Long projectId, int page) {
        Pageable pageable = PageRequest.of(page, 18);
        Page<ProposalEntity> proposalsPage = proposalRepository.findByToProjectId(projectId, pageable);
        return proposalsPage.stream()
                .map(ProposalResponse::new)
                .collect(Collectors.toList());
    }

    // 프로필에 달린 제안 리스트 조회
    public List<ProposalResponse> getProposalsByProfile(Long profileId, int page) {
        Pageable pageable = PageRequest.of(page, 18);
        Page<ProposalEntity> proposalsPage = proposalRepository.findByToProfileId(profileId, pageable);
        return proposalsPage.stream()
                .map(ProposalResponse::new)
                .collect(Collectors.toList());
    }

    // 단일 제안 조회
    public ProposalResponse getProposal(Long proposalId) {
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("제안을 찾을 수 없습니다."));
        return new ProposalResponse(proposal);
    }

    // 제안 삭제
    public void deleteProposal(Long proposalId) {
        if (!proposalRepository.existsById(proposalId)) {
            throw new RuntimeException("삭제할 제안이 존재하지 않습니다.");
        }
        proposalRepository.deleteById(proposalId);
    }
}
