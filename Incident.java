package com.acme.intelligence.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String severity;
    private String title;
    private String status;
    private Long serviceId;

    @Column(length = 3000)
    private String summary;

    private Instant createdAt;

    protected Incident() {}

    public Incident(
            String severity,
            String title,
            String status,
            Long serviceId,
            String summary,
            Instant createdAt
    ) {
        this.severity = severity;
        this.title = title;
        this.status = status;
        this.serviceId = serviceId;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getSeverity() {
        return severity;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getSummary() {
        return summary;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
