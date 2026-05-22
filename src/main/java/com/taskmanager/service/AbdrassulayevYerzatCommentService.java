package com.taskmanager.service;

import com.taskmanager.dto.request.AbdrassulayevYerzatCommentCreateRequest;
import com.taskmanager.dto.response.AbdrassulayevYerzatCommentResponse;
import com.taskmanager.entity.AbdrassulayevYerzatComment;
import com.taskmanager.entity.AbdrassulayevYerzatTask;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import com.taskmanager.exception.AbdrassulayevYerzatResourceNotFoundException;
import com.taskmanager.repository.AbdrassulayevYerzatCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbdrassulayevYerzatCommentService {

    private final AbdrassulayevYerzatCommentRepository commentRepository;
    private final AbdrassulayevYerzatUserService userService;
    private final AbdrassulayevYerzatTaskService taskService;

    public AbdrassulayevYerzatCommentResponse addComment(Long taskId, AbdrassulayevYerzatCommentCreateRequest request) {
        log.info("Adding comment to task: {}", taskId);

        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatTask task = taskService.findTaskEntity(taskId);

        AbdrassulayevYerzatComment comment = AbdrassulayevYerzatComment.builder()
                .content(request.getContent())
                .user(currentUser)
                .task(task)
                .build();

        AbdrassulayevYerzatComment savedComment = commentRepository.save(comment);
        log.info("Comment added with id: {}", savedComment.getId());

        return mapToResponse(savedComment);
    }

    public List<AbdrassulayevYerzatCommentResponse> getTaskComments(Long taskId) {
        AbdrassulayevYerzatTask task = taskService.findTaskEntity(taskId);
        List<AbdrassulayevYerzatComment> comments = commentRepository.findByTaskOrderByCreatedAtDesc(task);

        return comments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId) {
        AbdrassulayevYerzatUser currentUser = userService.getCurrentUser();
        AbdrassulayevYerzatComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AbdrassulayevYerzatResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            log.warn("User {} tried to delete comment of another user", currentUser.getUsername());
            throw new AbdrassulayevYerzatResourceNotFoundException("Comment", "id", commentId);
        }

        commentRepository.delete(comment);
        log.info("Comment deleted with id: {}", commentId);
    }

    private AbdrassulayevYerzatCommentResponse mapToResponse(AbdrassulayevYerzatComment comment) {
        return AbdrassulayevYerzatCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .taskId(comment.getTask().getId())
                .build();
    }
}