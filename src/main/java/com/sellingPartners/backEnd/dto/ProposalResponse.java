package com.sellingPartners.backEnd.dto;

import java.time.LocalDateTime;
import com.sellingPartners.backEnd.entity.ProposalEntity;
import lombok.Getter;

@Getter
public class ProposalResponse {

    private Long id;
    private Long fromUserId;
    private Long toProfileId;
    private Long toProjectId;
    private String message;
    private Integer proposedPrice;
    private LocalDateTime createdAt;

    public ProposalResponse(ProposalEntity proposal) {
        this.id = proposal.getId();
        this.fromUserId = proposal.getFromUser().getId();
        this.toProfileId = proposal.getToProfile() != null ? proposal.getToProfile().getId() : null;
        this.toProjectId = proposal.getToProject() != null ? proposal.getToProject().getId() : null;
        this.message = proposal.getMessage();
        this.proposedPrice = proposal.getProposedPrice();
        this.createdAt = proposal.getCreatedAt();
    }
}
