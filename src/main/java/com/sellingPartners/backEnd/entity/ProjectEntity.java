package com.sellingPartners.backEnd.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // 추가

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter // ← setter 전체 적용!
@Table(name = "projects") // 테이블명 지정
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "user_id")
    private UserEntity user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 300)
    private String summary;

    @Column(length = 2083)
    private String thumbnailUrl;

    @Column(length = 2083, name = "brochure_url")
    private String brochureUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDate deadline;

    @Column(columnDefinition = "boolean default true")
    private boolean status = true; // true: open, false: closed

    @Column(columnDefinition = "int default 0")
    private int views = 0;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public ProjectEntity(
        UserEntity user,
        String title,
        String description,
        String summary,
        String thumbnailUrl,
        String brochureUrl,
        Category category,
        LocalDate deadline,
        boolean status
    ) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.brochureUrl = brochureUrl;
        this.category = category;
        this.deadline = deadline;
        this.status = status;
    }
}
