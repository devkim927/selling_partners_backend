package com.sellingPartners.backEnd.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "proposal")
@Setter
public class ProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name = "to_profile_id", nullable = true)
    private PartnerProfileEntity toProfile;

    @ManyToOne
    @JoinColumn(name = "to_project_id", nullable = true)
    private ProjectEntity toProject;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Integer proposedPrice;

    private LocalDateTime createdAt;
}

