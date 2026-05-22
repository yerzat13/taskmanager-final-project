package com.taskmanager.service;

import com.taskmanager.dto.request.AbdrassulayevYerzatTaskCreateRequest;
import com.taskmanager.dto.request.AbdrassulayevYerzatTaskUpdateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatTaskResponse;
import com.taskmanager.entity.AbdrassulayevYerzatProject;
import com.taskmanager.entity.AbdrassulayevYerzatTask;
import com.taskmanager.entity.AbdrassulayevYerzatTask.Priority;
import com.taskmanager.entity.AbdrassulayevYerzatTask.Status;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import com.taskmanager.exception.AbdrassulayevYerzatResourceNotFoundException;
import com.taskmanager.mapper.AbdrassulayevYerzatTaskMapper;
import com.taskmanager.repository.AbdrassulayevYerzatTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.ArrayList;
@Slf4j
@Service
@RequiredArgsConstructor
public class AbdrassulayevYerzatTaskService {

    private final AbdrassulayevYerzatTaskRepository taskRepository;
    private final AbdrassulayevYerzatUserService userService;
    private final AbdrassulayevYerzatProjectService projectService;
    private final AbdrassulayevYerzatTaskMapper taskMapper;

    public AbdrassulayevYerzatTaskResponse createTask(AbdrassulayevYerzatTaskCreateRequest request) {
        log.info("Creating new task: {}", request.getTitle());

        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatTask task = taskMapper.toEntity(request);
        task.setUser(currentUser);
        task.setStatus(Status.TODO);

        if (request.getProjectId() != null) {
            try {
                AbdrassulayevYerzatProject project = projectService.findProjectEntity(request.getProjectId());
                task.setProject(project);
            } catch (Exception e) {
                log.warn("Project not found: {}", request.getProjectId());
            }
        }

        AbdrassulayevYerzatTask savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());

        return taskMapper.toResponse(savedTask);
    }

    public Page<AbdrassulayevYerzatTaskResponse> getUserTasksWithFilters(
            int page, int size, String sortBy, String sortDir,
            Status status, Priority priority, String search,
            LocalDateTime dueDateFrom, LocalDateTime dueDateTo) {

        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AbdrassulayevYerzatTask> tasks = taskRepository.findWithFilters(
                currentUser, status, priority, search, dueDateFrom, dueDateTo, pageable);

        return tasks.map(taskMapper::toResponse);
    }

    public AbdrassulayevYerzatTaskResponse getTask(Long id) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatTask task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Task", "id", id));

        return taskMapper.toResponse(task);
    }

    public List<AbdrassulayevYerzatTaskResponse> getAllTasks() {
        log.info("Getting all tasks for current user");
        try {
            AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
            log.info("Current user: {}", currentUser.getUsername());

            List<AbdrassulayevYerzatTask> tasks = taskRepository.findByUser(currentUser, PageRequest.of(0, 1000)).getContent();
            log.info("Found {} tasks for user", tasks.size());

            return tasks.stream()
                    .map(taskMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting tasks: ", e);
            return new ArrayList<>();
        }
    }
    public AbdrassulayevYerzatTaskResponse updateTask(Long id, AbdrassulayevYerzatTaskUpdateRequest request) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatTask task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Task", "id", id));

        taskMapper.updateEntity(task, request);

        if (request.getProjectId() != null) {
            try {
                AbdrassulayevYerzatProject project = projectService.findProjectEntity(request.getProjectId());
                task.setProject(project);
            } catch (Exception e) {
                log.warn("Project not found: {}", request.getProjectId());
            }
        }

        AbdrassulayevYerzatTask updatedTask = taskRepository.save(task);
        log.info("Task updated with id: {}", id);

        return taskMapper.toResponse(updatedTask);
    }

    public void deleteTask(Long id) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatTask task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Task", "id", id));

        taskRepository.delete(task);
        log.info("Task deleted with id: {}", id);
    }

    public AbdrassulayevYerzatTask findTaskEntity(Long id) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        return taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Task", "id", id));
    }

    @Async
    public CompletableFuture<Long> getTasksDueSoonCount() {
        log.info("Async counting tasks due soon");
        try {
            AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
            LocalDateTime soon = LocalDateTime.now().plusDays(3);
            LocalDateTime now = LocalDateTime.now();

            Page<AbdrassulayevYerzatTask> tasks = taskRepository.findWithFilters(
                    currentUser, Status.IN_PROGRESS, null, null, now, soon, PageRequest.of(0, 1000));

            return CompletableFuture.completedFuture((long) tasks.getContent().size());
        } catch (Exception e) {
            log.error("Error counting due soon tasks: {}", e.getMessage());
            return CompletableFuture.completedFuture(0L);
        }
    }
}