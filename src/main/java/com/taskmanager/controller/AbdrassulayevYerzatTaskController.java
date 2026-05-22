package com.taskmanager.controller;

import com.taskmanager.dto.request.AbdrassulayevYerzatTaskCreateRequest;
import com.taskmanager.dto.request.AbdrassulayevYerzatTaskUpdateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatTaskResponse;
import com.taskmanager.service.AbdrassulayevYerzatTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "Endpoints for managing tasks")
public class AbdrassulayevYerzatTaskController {

    private final AbdrassulayevYerzatTaskService taskService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new task")
    public ResponseEntity<AbdrassulayevYerzatTaskResponse> createTask(
            @Valid @RequestBody AbdrassulayevYerzatTaskCreateRequest request) {
        log.info("POST /api/tasks - Create task: {}", request.getTitle());
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all user tasks")
    public ResponseEntity<List<AbdrassulayevYerzatTaskResponse>> getAllTasks() {
        log.info("GET /api/tasks - Get all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<AbdrassulayevYerzatTaskResponse> getTask(@PathVariable Long id) {
        log.info("GET /api/tasks/{} - Get task", id);
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update task by ID")
    public ResponseEntity<AbdrassulayevYerzatTaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody AbdrassulayevYerzatTaskUpdateRequest request) {
        log.info("PUT /api/tasks/{} - Update task", id);
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete task by ID")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /api/tasks/{} - Delete task", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/due-soon")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get count of tasks due soon")
    public ResponseEntity<Long> getTasksDueSoonCount() {
        log.info("GET /api/tasks/stats/due-soon");
        return ResponseEntity.ok(0L);
    }
}