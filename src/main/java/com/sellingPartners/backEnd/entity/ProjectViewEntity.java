package com.sellingPartners.backEnd.entity;
import lombok.Setter;

import jakarta.persistence.*;


@Entity
@Setter
public class ProjectViewEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long projectId;
}