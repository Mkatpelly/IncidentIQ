package com.acme.intelligence.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String region;
    private String tier;
    protected Customer() {}

    public Customer(String name, String region, String tier) {
        this.name = name;
        this.region = region;
        this.tier = tier;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getTier() {
        return tier;
    }
}