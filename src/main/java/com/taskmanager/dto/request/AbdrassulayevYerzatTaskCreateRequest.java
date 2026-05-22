package com.taskmanager.dto.request;

import com.taskmanager.entity.AbdrassulayevYerzatTask.Priority;
import com.taskmanager.entity.AbdrassulayevYerzatTask.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbdrassulayevYerzatTaskCreateRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime dueDate;

    private Long projectId;
}