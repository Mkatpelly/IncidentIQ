package com.acme.intelligence.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class ServiceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String ownerTeam;
    private String status;

    protected ServiceStatus() {}

    public ServiceStatus(String name, String ownerTeam, String status) {
        this.name = name;
        this.ownerTeam = ownerTeam;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerTeam() {
        return ownerTeam;
    }

    public String getStatus() {
        return status;
    }
}