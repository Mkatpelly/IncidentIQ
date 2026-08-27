package com.acme.intelligence.service;

import com.acme.intelligence.domain.Payment;
import com.acme.intelligence.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class AnomalyDetectionService {

    private final PaymentRepository paymentRepository;

    public AnomalyDetectionService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Map<String, Object> detectPaymentFailureAnomaly() {
        Instant now = Instant.now();
        Instant recentStart = now.minus(Duration.ofDays(7));
        Instant baselineStart = recentStart.minus(Duration.ofDays(7));

        List<Payment> recentPayments =
                paymentRepository.findByCreatedAtBetween(recentStart, now);

        List<Payment> baselinePayments =
                paymentRepository.findByCreatedAtBetween(baselineStart, recentStart);

        double currentRate = failureRate(recentPayments);
        double baselineRate = failureRate(baselinePayments);
        double deviation = currentRate - baselineRate;

        String risk = "LOW";

        if (deviation > 0.08) {
            risk = "HIGH";
        } else if (deviation > 0.03) {
            risk = "MEDIUM";
        }

        return Map.of(
                "risk", risk,
                "currentFailureRate", round(currentRate * 100),
                "expectedBaseline", round(baselineRate * 100),
                "deviationPercentagePoints", round(deviation * 100),
                "recentTransactionCount", recentPayments.size(),
                "baselineTransactionCount", baselinePayments.size()
        );
    }

    private double failureRate(List<Payment> payments) {
        if (payments.isEmpty()) {
            return 0.0;
        }

        long failures = payments.stream()
                .filter(payment -> "failed".equalsIgnoreCase(payment.getStatus()))
                .count();

        return (double) failures / payments.size();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
