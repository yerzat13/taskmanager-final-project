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
public class AbdrassulayevYerzatCommentResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private Long taskId;
}