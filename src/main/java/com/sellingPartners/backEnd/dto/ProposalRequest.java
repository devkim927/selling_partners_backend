package com.sellingPartners.backEnd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalRequest {
    private Long toProfileId;
    private Long toProjectId;
    private String message;
    private Integer proposedPrice;
    
    public boolean isValid() {
        return (toProfileId != null && toProjectId == null) ||
               (toProfileId == null && toProjectId != null);
    }
}
