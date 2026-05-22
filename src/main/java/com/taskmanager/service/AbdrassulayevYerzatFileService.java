package com.taskmanager.service;

import com.taskmanager.entity.AbdrassulayevYerzatAttachment;
import com.taskmanager.entity.AbdrassulayevYerzatTask;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import com.taskmanager.exception.AbdrassulayevYerzatBadRequestException;
import com.taskmanager.exception.AbdrassulayevYerzatResourceNotFoundException;
import com.taskmanager.repository.AbdrassulayevYerzatAttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbdrassulayevYerzatFileService {

    private final AbdrassulayevYerzatAttachmentRepository attachmentRepository;
    private final AbdrassulayevYerzatUserService userService;
    private final AbdrassulayevYerzatTaskService taskService;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    public AbdrassulayevYerzatAttachment uploadFile(Long taskId, MultipartFile file) {
        log.info("Uploading file to task: {}", taskId);

        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatTask task = taskService.findTaskEntity(taskId);

        if (file.isEmpty()) {
            throw new AbdrassulayevYerzatBadRequestException("File is empty");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String storedFilename = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), filePath);

            AbdrassulayevYerzatAttachment attachment = AbdrassulayevYerzatAttachment.builder()
                    .fileName(originalFilename)
                    .filePath(filePath.toString())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .task(task)
                    .user(currentUser)
                    .build();

            AbdrassulayevYerzatAttachment savedAttachment = attachmentRepository.save(attachment);
            log.info("File uploaded successfully: {}", savedAttachment.getFileName());

            return savedAttachment;

        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new AbdrassulayevYerzatBadRequestException("Failed to upload file: " + e.getMessage());
        }
    }

    public Resource downloadFile(Long attachmentId) {
        log.info("Downloading file with id: {}", attachmentId);

        AbdrassulayevYerzatAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Attachment", "id", attachmentId));

        try {
            Path filePath = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new AbdrassulayevYerzatResourceNotFoundException("File not found");
            }
        } catch (MalformedURLException e) {
            log.error("Failed to download file: {}", e.getMessage());
            throw new AbdrassulayevYerzatBadRequestException("Failed to download file");
        }
    }

    public List<AbdrassulayevYerzatAttachment> getTaskAttachments(Long taskId) {
        AbdrassulayevYerzatTask task = taskService.findTaskEntity(taskId);
        return attachmentRepository.findByTask(task);
    }

    public void deleteAttachment(Long attachmentId) {
        AbdrassulayevYerzatAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Attachment", "id", attachmentId));

        try {
            Path filePath = Paths.get(attachment.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Failed to delete physical file: {}", e.getMessage());
        }

        attachmentRepository.delete(attachment);
        log.info("Attachment deleted with id: {}", attachmentId);
    }

    @Async
    public CompletableFuture<Long> getTotalStorageUsedByUser() {
        log.info("Async calculating total storage used by user");
        try {
            AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
            List<AbdrassulayevYerzatAttachment> attachments = attachmentRepository.findAll();

            long totalSize = attachments.stream()
                    .filter(a -> a.getUser() != null && a.getUser().getId().equals(currentUser.getId()))
                    .mapToLong(AbdrassulayevYerzatAttachment::getFileSize)
                    .sum();

            return CompletableFuture.completedFuture(totalSize);
        } catch (Exception e) {
            log.error("Error calculating storage used: {}", e.getMessage());
            return CompletableFuture.completedFuture(0L);
        }
    }
}