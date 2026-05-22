package com.taskmanager.service;

import com.taskmanager.entity.AbdrassulayevYerzatAuditLog;
import com.taskmanager.repository.AbdrassulayevYerzatAuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbdrassulayevYerzatAuditService {

    private final AbdrassulayevYerzatAuditLogRepository auditLogRepository;

    @Async
    public void logAction(String action, String username, String details, String ipAddress) {
        try {
            AbdrassulayevYerzatAuditLog auditLog = AbdrassulayevYerzatAuditLog.builder()
                    .action(action)
                    .username(username)
                    .details(details)
                    .ipAddress(ipAddress)
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log saved: {} - {}", action, username);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }
}