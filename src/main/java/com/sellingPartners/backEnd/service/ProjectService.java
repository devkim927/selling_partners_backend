package com.sellingPartners.backEnd.service;

import com.sellingPartners.backEnd.dto.ProjectCreateRequest;
import com.sellingPartners.backEnd.entity.ProjectEntity;
import com.sellingPartners.backEnd.entity.ProjectViewEntity;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.entity.Category;
import com.sellingPartners.backEnd.entity.Role;
import com.sellingPartners.backEnd.repository.ProjectRepository;
import com.sellingPartners.backEnd.repository.ProjectViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Objects;
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FileStorageService fileStorageService;
    private final ProjectViewRepository projectViewRepository;
    // 프로젝트 생성 (로그인 필요)
    @Transactional
    public ProjectEntity createProject(ProjectCreateRequest request, UserEntity user) throws Exception {
    	// 역할 검증: COMPANY만 생성 가능
    	if (user.getRole() != Role.COMPANY) {
            throw new AccessDeniedException("COMPANY 고객만 프로젝트를 생성할 수 있습니다.");
        }

        // 카테고리 기본값 설정
        Category category = request.getCategory() != null 
            ? request.getCategory() 
            : Category.IT_SOFTWARE;

        // 프로젝트 빌더 생성
        ProjectEntity.ProjectEntityBuilder projectBuilder = ProjectEntity.builder()
            .user(user)
            .title(request.getTitle())
            .description(request.getDescription())
            .summary(request.getSummary())
            .category(category)
            .deadline(request.getDeadline())
            .status(true);

        // 썸네일 업로드
        if (request.getThumbnail() != null && !request.getThumbnail().isEmpty()) {
            projectBuilder.thumbnailUrl(
                fileStorageService.storeFile(request.getThumbnail(), "thumbnails")
            );
        }

        // 브로셔 업로드
        if (request.getBrochure() != null && !request.getBrochure().isEmpty()) {
            projectBuilder.brochureUrl(
                fileStorageService.storeFile(request.getBrochure(), "brochures")
            );
        }

        return projectRepository.save(projectBuilder.build());
    }

    // 프로젝트 수정 (로그인 필요 + 본인 소유만 가능)
    @Transactional
    public ProjectEntity updateProject(Long id, ProjectCreateRequest request, UserEntity user) throws Exception {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // 소유자 검증 (equals 사용 권장)
        if (!Objects.equals(project.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("본인만 수정할 수 있습니다.");
        }

        // === 부분 수정: null 체크 후 업데이트 ===
        if (request.getTitle() != null) {
            project.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getSummary() != null) {
            project.setSummary(request.getSummary());
        }
        if (request.getCategory() != null) {
            project.setCategory(request.getCategory());
        }
        if (request.getDeadline() != null) {
            project.setDeadline(request.getDeadline());
        }

        // 썸네일 파일 수정
        if (request.getThumbnail() != null && !request.getThumbnail().isEmpty()) {
            if (project.getThumbnailUrl() != null) {
                fileStorageService.deleteFile(project.getThumbnailUrl());
            }
            String newThumbnailUrl = fileStorageService.storeFile(request.getThumbnail(), "thumbnails");
            project.setThumbnailUrl(newThumbnailUrl);
        }

        // 브로셔 파일 수정
        if (request.getBrochure() != null && !request.getBrochure().isEmpty()) {
            if (project.getBrochureUrl() != null) {
                fileStorageService.deleteFile(project.getBrochureUrl());
            }
            String newBrochureUrl = fileStorageService.storeFile(request.getBrochure(), "brochures");
            project.setBrochureUrl(newBrochureUrl);
        }

        // save()는 트랜잭션 커밋 시 자동 반영되지만, 명시적으로 호출해도 무방
        return projectRepository.save(project);
    }

    // 프로젝트 단일 조회 (누구나 가능)
    public Optional<ProjectEntity> getProjectById(Long id, UserEntity user) {
        Optional<ProjectEntity> projectOpt = projectRepository.findById(id);

        if (user != null && projectOpt.isPresent()) {
            Long userId = user.getId();

            // 이미 조회했는지 확인
            boolean alreadyViewed = projectViewRepository.existsByUserIdAndProjectId(userId, id);

            if (!alreadyViewed) {
                // 조회수 +1
                ProjectEntity project = projectOpt.get();
                project.setViews(project.getViews() + 1);
                projectRepository.save(project);

                // 조회 기록 저장
                ProjectViewEntity view = new ProjectViewEntity();
                view.setUserId(userId);
                view.setProjectId(id);
                projectViewRepository.save(view);
            }
        }

        return projectOpt;
    }

    // 프로젝트 전체 조회 (누구나 가능)
    public Page<ProjectEntity> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    // 프로젝트 삭제 (로그인 필요 + 본인 소유만 가능)
    @Transactional
    public void deleteProject(Long id, UserEntity user) throws Exception {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // 소유자 검증
        if (project.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("본인만 삭제할 수 있습니다.");
        }
        
        // 파일 삭제
        fileStorageService.deleteFile(project.getThumbnailUrl());
        fileStorageService.deleteFile(project.getBrochureUrl());
        
        // 엔티티 삭제
        projectRepository.deleteById(id);
    }
    
    public Page<ProjectEntity> getMyProjects(Pageable pageable, UserEntity user) {
        // user가 만든 프로젝트만 반환
        return projectRepository.findAllByUser(user, pageable);
    }
}
