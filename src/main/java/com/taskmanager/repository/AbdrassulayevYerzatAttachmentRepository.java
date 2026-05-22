package com.taskmanager.repository;

import com.taskmanager.entity.AbdrassulayevYerzatAttachment;
import com.taskmanager.entity.AbdrassulayevYerzatTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbdrassulayevYerzatAttachmentRepository extends JpaRepository<AbdrassulayevYerzatAttachment, Long> {

    List<AbdrassulayevYerzatAttachment> findByTask(AbdrassulayevYerzatTask task);

    Optional<AbdrassulayevYerzatAttachment> findByIdAndTask_Id(Long id, Long taskId);

    void deleteByTask(AbdrassulayevYerzatTask task);
}