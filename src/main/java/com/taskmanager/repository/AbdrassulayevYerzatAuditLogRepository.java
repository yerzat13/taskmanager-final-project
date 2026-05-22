package com.taskmanager.repository;

import com.taskmanager.entity.AbdrassulayevYerzatAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbdrassulayevYerzatAuditLogRepository extends JpaRepository<AbdrassulayevYerzatAuditLog, Long> {
}