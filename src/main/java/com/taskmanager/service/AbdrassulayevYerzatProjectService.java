package com.taskmanager.service;

import com.taskmanager.dto.request.AbdrassulayevYerzatProjectCreateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatProjectResponse;
import com.taskmanager.entity.AbdrassulayevYerzatProject;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import com.taskmanager.exception.AbdrassulayevYerzatResourceNotFoundException;
import com.taskmanager.mapper.AbdrassulayevYerzatProjectMapper;
import com.taskmanager.repository.AbdrassulayevYerzatProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbdrassulayevYerzatProjectService {

    private final AbdrassulayevYerzatProjectRepository projectRepository;
    private final AbdrassulayevYerzatUserService userService;
    private final AbdrassulayevYerzatProjectMapper projectMapper;

    public AbdrassulayevYerzatProjectResponse createProject(AbdrassulayevYerzatProjectCreateRequest request) {
        log.info("Creating new project: {}", request.getName());

        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatProject project = projectMapper.toEntity(request);
        project.setOwner(currentUser);

        AbdrassulayevYerzatProject savedProject = projectRepository.save(project);
        log.info("Project created successfully with id: {}", savedProject.getId());

        return projectMapper.toResponse(savedProject);
    }

    public Page<AbdrassulayevYerzatProjectResponse> getUserProjects(int page, int size, String sortBy, String sortDir, String search) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AbdrassulayevYerzatProject> projects;
        if (search != null && !search.isEmpty()) {
            projects = projectRepository.searchByOwnerAndKeyword(currentUser, search, pageable);
        } else {
            projects = projectRepository.findByOwner(currentUser, pageable);
        }

        return projects.map(projectMapper::toResponse);
    }

    public AbdrassulayevYerzatProjectResponse getProject(Long id) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatProject project = projectRepository.findByIdAndOwner(id, currentUser)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Project", "id", id));

        return projectMapper.toResponse(project);
    }

    public void deleteProject(Long id) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatProject project = projectRepository.findByIdAndOwner(id, currentUser)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Project", "id", id));

        projectRepository.delete(project);
        log.info("Project deleted with id: {}", id);
    }

    public AbdrassulayevYerzatProject findProjectEntity(Long id) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        return projectRepository.findByIdAndOwner(id, currentUser)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Project", "id", id));
    }

    @Async
    public CompletableFuture<Long> getProjectTaskCountAsync(Long projectId) {
        log.info("Async counting tasks for project: {}", projectId);
        try {
            AbdrassulayevYerzatProject project = findProjectEntity(projectId);
            long count = project.getTasks() != null ? project.getTasks().size() : 0;
            return CompletableFuture.completedFuture(count);
        } catch (Exception e) {
            log.error("Error counting tasks for project {}: {}", projectId, e.getMessage());
            return CompletableFuture.completedFuture(0L);
        }
    }
}