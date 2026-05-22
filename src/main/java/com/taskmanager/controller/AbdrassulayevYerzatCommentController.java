package com.taskmanager.controller;

import com.taskmanager.dto.request.AbdrassulayevYerzatCommentCreateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatCommentResponse;
import com.taskmanager.service.AbdrassulayevYerzatCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Management", description = "Endpoints for managing task comments")
public class AbdrassulayevYerzatCommentController {

    private final AbdrassulayevYerzatCommentService commentService;

    @PostMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add comment to task")
    public ResponseEntity<AbdrassulayevYerzatCommentResponse> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody AbdrassulayevYerzatCommentCreateRequest request) {

        log.info("POST /api/comments/task/{} - Add comment", taskId);
        return ResponseEntity.ok(commentService.addComment(taskId, request));
    }

    @GetMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all comments for a task")
    public ResponseEntity<List<AbdrassulayevYerzatCommentResponse>> getTaskComments(
            @PathVariable Long taskId) {

        log.info("GET /api/comments/task/{} - Get comments", taskId);
        return ResponseEntity.ok(commentService.getTaskComments(taskId));
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete comment by ID")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId) {

        log.info("DELETE /api/comments/{} - Delete comment", commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}