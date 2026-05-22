package com.taskmanager.repository;

import com.taskmanager.entity.AbdrassulayevYerzatComment;
import com.taskmanager.entity.AbdrassulayevYerzatTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbdrassulayevYerzatCommentRepository extends JpaRepository<AbdrassulayevYerzatComment, Long> {

    List<AbdrassulayevYerzatComment> findByTaskOrderByCreatedAtDesc(AbdrassulayevYerzatTask task);

    void deleteByTask(AbdrassulayevYerzatTask task);
}