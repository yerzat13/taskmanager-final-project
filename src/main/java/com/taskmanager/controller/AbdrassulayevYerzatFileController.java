package com.taskmanager.controller;

import com.taskmanager.dto.response.AbdrassulayevYerzatAttachmentResponse;
import com.taskmanager.entity.AbdrassulayevYerzatAttachment;
import com.taskmanager.service.AbdrassulayevYerzatFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Management", description = "Endpoints for file upload/download with async operations")
public class AbdrassulayevYerzatFileController {

    private final AbdrassulayevYerzatFileService fileService;

    @PostMapping("/upload/task/{taskId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload file to task")
    public ResponseEntity<AbdrassulayevYerzatAttachment> uploadFile(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file) {

        log.info("POST /api/files/upload/task/{} - Upload file: {}", taskId, file.getOriginalFilename());
        return ResponseEntity.ok(fileService.uploadFile(taskId, file));
    }

    @GetMapping("/download/{attachmentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Download file by attachment ID")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long attachmentId) {
        log.info("GET /api/files/download/{} - Download file", attachmentId);

        Resource resource = fileService.downloadFile(attachmentId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachmentId + "\"")
                .body(resource);
    }

    @GetMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all attachments for a task")
    public ResponseEntity<List<AbdrassulayevYerzatAttachmentResponse>> getTaskAttachments(@PathVariable Long taskId) {
        log.info("GET /api/files/task/{} - Get attachments", taskId);

        List<AbdrassulayevYerzatAttachment> attachments = fileService.getTaskAttachments(taskId);

        List<AbdrassulayevYerzatAttachmentResponse> response = attachments.stream()
                .map(a -> AbdrassulayevYerzatAttachmentResponse.builder()
                        .id(a.getId())
                        .fileName(a.getFileName())
                        .fileType(a.getFileType())
                        .fileSize(a.getFileSize())
                        .uploadedAt(a.getUploadedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{attachmentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete attachment by ID")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        log.info("DELETE /api/files/{} - Delete attachment", attachmentId);
        fileService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/storage")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get total storage used by user (async)")
    public ResponseEntity<Long> getTotalStorageUsed() {
        log.info("GET /api/files/stats/storage");
        try {
            CompletableFuture<Long> future = fileService.getTotalStorageUsedByUser();
            Long storage = future.get();
            return ResponseEntity.ok(storage);
        } catch (Exception e) {
            log.error("Error getting storage: {}", e.getMessage());
            return ResponseEntity.ok(0L);
        }
    }
}