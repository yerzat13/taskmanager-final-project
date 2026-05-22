package com.taskmanager.repository;

import com.taskmanager.entity.AbdrassulayevYerzatTask;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import com.taskmanager.entity.AbdrassulayevYerzatProject;
import com.taskmanager.entity.AbdrassulayevYerzatTask.Status;
import com.taskmanager.entity.AbdrassulayevYerzatTask.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AbdrassulayevYerzatTaskRepository extends JpaRepository<AbdrassulayevYerzatTask, Long> {

    Page<AbdrassulayevYerzatTask> findByUser(AbdrassulayevYerzatUser user, Pageable pageable);

    Page<AbdrassulayevYerzatTask> findByProject(AbdrassulayevYerzatProject project, Pageable pageable);

    List<AbdrassulayevYerzatTask> findByUserAndStatus(AbdrassulayevYerzatUser user, Status status);

    Optional<AbdrassulayevYerzatTask> findByIdAndUser(Long id, AbdrassulayevYerzatUser user);

    @Query("SELECT t FROM AbdrassulayevYerzatTask t WHERE t.user = :user AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:dueDateFrom IS NULL OR t.dueDate >= :dueDateFrom) AND " +
            "(:dueDateTo IS NULL OR t.dueDate <= :dueDateTo)")
    Page<AbdrassulayevYerzatTask> findWithFilters(@Param("user") AbdrassulayevYerzatUser user,
                                                  @Param("status") Status status,
                                                  @Param("priority") Priority priority,
                                                  @Param("search") String search,
                                                  @Param("dueDateFrom") LocalDateTime dueDateFrom,
                                                  @Param("dueDateTo") LocalDateTime dueDateTo,
                                                  Pageable pageable);
}