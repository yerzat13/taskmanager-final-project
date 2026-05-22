package com.taskmanager.controller;

import com.taskmanager.dto.request.AbdrassulayevYerzatProjectCreateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatProjectResponse;
import com.taskmanager.service.AbdrassulayevYerzatProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "Endpoints for managing projects")
public class AbdrassulayevYerzatProjectController {

    private final AbdrassulayevYerzatProjectService projectService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new project")
    public ResponseEntity<AbdrassulayevYerzatProjectResponse> createProject(
            @Valid @RequestBody AbdrassulayevYerzatProjectCreateRequest request) {

        log.info("POST /api/projects - Create project: {}", request.getName());
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get user projects with pagination, sorting and search")
    public ResponseEntity<Page<AbdrassulayevYerzatProjectResponse>> getUserProjects(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String search) {

        log.info("GET /api/projects - page={}, size={}, sortBy={}, sortDir={}, search={}",
                page, size, sortBy, sortDir, search);

        return ResponseEntity.ok(projectService.getUserProjects(page, size, sortBy, sortDir, search));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get project by ID")
    public ResponseEntity<AbdrassulayevYerzatProjectResponse> getProject(
            @PathVariable Long id) {

        log.info("GET /api/projects/{} - Get project", id);
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete project by ID")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id) {

        log.info("DELETE /api/projects/{} - Delete project", id);
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}