package com.sellingPartners.backEnd.controller;

import com.sellingPartners.backEnd.dto.ProjectCreateRequest;
import com.sellingPartners.backEnd.entity.ProjectEntity;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.entity.Category;
import com.sellingPartners.backEnd.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProjectEntity> createProject(
        @ModelAttribute ProjectCreateRequest request,
        @AuthenticationPrincipal UserEntity user
    ) {
        try {
            if (request.getCategory() == null) {
                request.setCategory(Category.IT_SOFTWARE);
            }
            ProjectEntity project = projectService.createProject(request, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(project);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 프로젝트 수정
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProjectEntity> updateProject(
        @PathVariable Long id,
        @ModelAttribute ProjectCreateRequest request,
        @AuthenticationPrincipal UserEntity user
    ) {
        try {
            ProjectEntity updatedProject = projectService.updateProject(id, request, user);
            return ResponseEntity.ok(updatedProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 프로젝트 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable Long id) {
        Optional<ProjectEntity> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 프로젝트 전체 조회
    @GetMapping
    public List<ProjectEntity> getAllProjects() {
        return projectService.getAllProjects();
    }

    // 프로젝트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
