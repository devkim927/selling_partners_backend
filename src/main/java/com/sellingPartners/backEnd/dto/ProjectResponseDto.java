package com.sellingPartners.backEnd.dto;

import com.sellingPartners.backEnd.entity.Category;
import com.sellingPartners.backEnd.entity.ProjectEntity;
import com.sellingPartners.backEnd.entity.UserEntity;



import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ProjectResponseDto {
    private Long id;
    private UserInfoDto user; // 사용자 정보 DTO로 대체
    private String title;
    private String description;
    private String summary;
    private String thumbnailUrl;
    private String brochureUrl;
    private Category category;
    private LocalDate deadline;
    private boolean status;
    private int views;
    private LocalDateTime createdAt;

    // 엔티티 → DTO 변환 생성자
    public ProjectResponseDto(ProjectEntity entity) {
        this.id = entity.getId();
        this.user = convertToUserInfoDto(entity.getUser());
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.summary = entity.getSummary();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.brochureUrl = entity.getBrochureUrl();
        this.category = entity.getCategory();
        this.deadline = entity.getDeadline();
        this.status = entity.isStatus();
        this.views = entity.getViews();
        this.createdAt = entity.getCreatedAt();
    }

    // UserEntity → UserInfoDto 변환
    private UserInfoDto convertToUserInfoDto(UserEntity userEntity) {
        if (userEntity == null) return null;
        
        return new UserInfoDto(
            userEntity.getName(),
            userEntity.getEmail(),
            userEntity.getPhoneNumber(),
            userEntity.getRole()
        );
    }
}
