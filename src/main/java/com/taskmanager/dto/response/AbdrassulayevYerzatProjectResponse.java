package com.taskmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbdrassulayevYerzatProjectResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long ownerId;
    private String ownerName;
    private Integer taskCount;
}