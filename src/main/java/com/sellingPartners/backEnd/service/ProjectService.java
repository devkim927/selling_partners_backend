package com.sellingPartners.backEnd.service;

import com.sellingPartners.backEnd.dto.ProjectCreateRequest;
import com.sellingPartners.backEnd.entity.ProjectEntity;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.entity.Category;
import com.sellingPartners.backEnd.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FileStorageService fileStorageService;

    // 프로젝트 생성
    @Transactional
    public ProjectEntity createProject(ProjectCreateRequest request, UserEntity user) throws Exception {
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

    // 프로젝트 수정
    @Transactional
    public ProjectEntity updateProject(Long id, ProjectCreateRequest request, UserEntity user) throws Exception {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // 기본 필드 업데이트
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setSummary(request.getSummary());
        project.setCategory(request.getCategory());
        project.setDeadline(request.getDeadline());

        // 썸네일 업데이트
        if (request.getThumbnail() != null && !request.getThumbnail().isEmpty()) {
            // 기존 썸네일 삭제
            fileStorageService.deleteFile(project.getThumbnailUrl());
            // 새 썸네일 저장
            String newThumbnailUrl = fileStorageService.storeFile(request.getThumbnail(), "thumbnails");
            project.setThumbnailUrl(newThumbnailUrl);
        }

        // 브로셔 업데이트
        if (request.getBrochure() != null && !request.getBrochure().isEmpty()) {
            // 기존 브로셔 삭제
            fileStorageService.deleteFile(project.getBrochureUrl());
            // 새 브로셔 저장
            String newBrochureUrl = fileStorageService.storeFile(request.getBrochure(), "brochures");
            project.setBrochureUrl(newBrochureUrl);
        }

        return projectRepository.save(project);
    }

    // 프로젝트 단일 조회
    public Optional<ProjectEntity> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    // 프로젝트 전체 조회
    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    // 프로젝트 삭제
    @Transactional
    public void deleteProject(Long id) throws Exception {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        
        // 파일 삭제
        fileStorageService.deleteFile(project.getThumbnailUrl());
        fileStorageService.deleteFile(project.getBrochureUrl());
        
        // 엔티티 삭제
        projectRepository.deleteById(id);
    }
}
