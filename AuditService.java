package com.acme.intelligence.service;

import com.acme.intelligence.domain.AuditLog;
import com.acme.intelligence.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(
            String actor,
            String action,
            String resource,
            boolean allowed,
            String detail
    ) {
        auditLogRepository.save(
                new AuditLog(actor, action, resource, allowed, detail)
        );
    }
}
