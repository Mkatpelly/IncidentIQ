package com.acme.intelligence.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long serviceId;

    private BigDecimal amount;

    private String status;
    private String errorCode;
    private String region;

    private Instant createdAt;

    protected Payment() {}

    public Payment(
            Long customerId,
            Long serviceId,
            BigDecimal amount,
            String status,
            String errorCode,
            String region,
            Instant createdAt
    ) {
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.amount = amount;
        this.status = status;
        this.errorCode = errorCode;
        this.region = region;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getRegion() {
        return region;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}