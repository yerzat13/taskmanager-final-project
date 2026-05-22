package com.taskmanager.dto.request;

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
public class AbdrassulayevYerzatTaskUpdateRequest {

    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime dueDate;

    private Long projectId;
}