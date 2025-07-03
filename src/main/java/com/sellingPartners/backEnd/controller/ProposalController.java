package com.sellingPartners.backEnd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellingPartners.backEnd.dto.ProposalRequest;
import com.sellingPartners.backEnd.dto.ProposalResponse;
import com.sellingPartners.backEnd.service.ProposalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/proposals")
@RequiredArgsConstructor
public class ProposalController {
	
  private final ProposalService proposalService;
  
  // 프로젝트 제안 리스트 조회
  @GetMapping("/projects/{projectId}/proposals")
  public ResponseEntity<List<ProposalResponse>> getProposalsByProject(
          @PathVariable Long projectId,
          @RequestParam(defaultValue = "0") int page) {
      List<ProposalResponse> proposals = proposalService.getProposalsByProject(projectId, page);
      return ResponseEntity.ok(proposals);
  }

  // 프로젝트에 새 제안 작성
  @PostMapping("/projects/{projectId}/proposals")
  public ResponseEntity<Long> createProposalForProject(@PathVariable Long projectId,
                                                      @RequestBody ProposalRequest dto,
                                                      @RequestParam Long fromUserId) {
      dto.setToProjectId(projectId);
      Long id = proposalService.createProposal(fromUserId, dto);
      return ResponseEntity.ok(id);
  }

  // 프로필 제안 리스트 조회
  @GetMapping("/profiles/{profileId}/proposals")
  public ResponseEntity<List<ProposalResponse>> getProposalsByProfile(
          @PathVariable Long profileId,
          @RequestParam(defaultValue = "0") int page) {
      List<ProposalResponse> proposals = proposalService.getProposalsByProfile(profileId, page);
      return ResponseEntity.ok(proposals);
  }

  // 프로필에 새 제안 작성
  @PostMapping("/profiles/{profileId}/proposals")
  public ResponseEntity<Long> createProposalForProfile(@PathVariable Long profileId,
                                                      @RequestBody ProposalRequest dto,
                                                      @RequestParam Long fromUserId) {
      dto.setToProfileId(profileId);
      Long id = proposalService.createProposal(fromUserId, dto);
      return ResponseEntity.ok(id);
  }

  // 개별 제안 조회
  @GetMapping("/proposals/{proposalId}")
  public ResponseEntity<ProposalResponse> getProposal(@PathVariable Long proposalId) {
      return ResponseEntity.ok(proposalService.getProposal(proposalId));
  }

  // 개별 제안 삭제
  @DeleteMapping("/proposals/{proposalId}")
  public ResponseEntity<Void> deleteProposal(@PathVariable Long proposalId) {
      proposalService.deleteProposal(proposalId);
      return ResponseEntity.noContent().build();
  }
}
