package com.taskmanager.dto.response;

import com.taskmanager.entity.AbdrassulayevYerzatTask.Priority;
import com.taskmanager.entity.AbdrassulayevYerzatTask.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbdrassulayevYerzatTaskResponse {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String username;
    private Long projectId;
    private String projectName;
    private Integer commentCount;
    private Integer attachmentCount;
}