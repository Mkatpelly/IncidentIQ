package com.acme.intelligence.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "deployments")
public class Deployment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long serviceId;
    private String version;
    private String deployedBy;
    private String notes;
    private Instant createdAt;

    protected Deployment() {}

    public Deployment(
            Long serviceId,
            String version,
            String deployedBy,
            String notes,
            Instant createdAt
    ) {
        this.serviceId = serviceId;
        this.version = version;
        this.deployedBy = deployedBy;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getVersion() {
        return version;
    }

    public String getDeployedBy() {
        return deployedBy;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
