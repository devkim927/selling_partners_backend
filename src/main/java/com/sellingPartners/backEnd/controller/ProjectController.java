package com.sellingPartners.backEnd.controller;

// Dto import
import com.sellingPartners.backEnd.dto.ProjectCreateRequest;
import com.sellingPartners.backEnd.dto.ProjectResponseDto;
// Entity import
import com.sellingPartners.backEnd.entity.ProjectEntity;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.entity.Category;
import com.sellingPartners.backEnd.service.FileStorageService;
import com.sellingPartners.backEnd.service.ProjectService;
import com.sellingPartners.backEnd.service.UserService;
import com.sellingPartners.backEnd.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
// 페이지네이션 임포트
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
// 스프링 security 
import org.springframework.security.access.AccessDeniedException; // 스프링 권한 표준 예외



@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectEntity> createProject(
        @ModelAttribute ProjectCreateRequest request
    ) {
        try {
        	
        	UserEntity user = userService.getCurrentUser();
            // 1. COMPANY 역할 검증
            if (user.getRole() != Role.COMPANY) {
                throw new AccessDeniedException("COMPANY 역할만 프로젝트를 생성할 수 있습니다.");
            }

            // 2. 카테고리 기본값 설정
            if (request.getCategory() == null) {
                request.setCategory(Category.IT_SOFTWARE);
            }
            
            // 3. 프로젝트 생성
            ProjectEntity project = projectService.createProject(request, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(project);
            
        } catch (AccessDeniedException e) {
            // COMPANY 역할이 아닌 경우 403 에러
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 프로젝트 수정 (본인 소유만 가능)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectEntity> updateProject(
    	@PathVariable("id") Long id,
        @ModelAttribute ProjectCreateRequest request
    ) {
        try {
        	UserEntity user = userService.getCurrentUser();
            ProjectEntity updatedProject = projectService.updateProject(id, request, user);
            return ResponseEntity.ok(updatedProject);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 403 에러
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable("id") Long id) {
        Optional<ProjectEntity> projectOpt = projectService.getProjectById(id);
        return projectOpt
            .map(project -> ResponseEntity.ok(new ProjectResponseDto(project)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public Page<ProjectResponseDto> getAllProjects(
        @PageableDefault(size = 18) Pageable pageable
    ) {
        Page<ProjectEntity> entityPage = projectService.getAllProjects(pageable);
        // 엔티티 -> DTO 변환
        return entityPage.map(ProjectResponseDto::new);
    }

    // 프로젝트 삭제 (본인 소유만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
    	@PathVariable("id") Long id
    ) {
        try {
        	UserEntity user = userService.getCurrentUser();
            projectService.deleteProject(id, user);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 에러
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
