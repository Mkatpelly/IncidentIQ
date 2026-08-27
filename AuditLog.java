package com.acme.intelligence.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actor;
    private String action;
    private String resource;
    private boolean allowed;

    @Column(length = 3000)
    private String detail;

    private Instant createdAt;

    protected AuditLog() {}

    public AuditLog(
            String actor,
            String action,
            String resource,
            boolean allowed,
            String detail
    ) {
        this.actor = actor;
        this.action = action;
        this.resource = resource;
        this.allowed = allowed;
        this.detail = detail;
        this.createdAt = Instant.now();
    }
}