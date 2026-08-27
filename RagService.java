package com.acme.intelligence.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class RagService {

    private final List<Document> documents = List.of(
            new Document(
                    "Payment Authorization Runbook",
                    "runbook/payment-auth",
                    """
                    When payment authorization errors exceed 10%, inspect recent
                    deployments, payment gateway health, region-specific spikes,
                    and retry configuration changes before communicating mitigation.
                    """
            ),
            new Document(
                    "Incident 482",
                    "incident/482",
                    """
                    A regression in Payment API v2.3 caused elevated declines in
                    US-West after a production configuration change.
                    """
            ),
            new Document(
                    "Payment API Architecture",
                    "architecture/payment-api",
                    """
                    Payment API depends on the gateway adapter, authorization service,
                    risk engine, and retry orchestration components.
                    """
            )
    );

    public List<Document> search(String query) {
        String normalized = query.toLowerCase(Locale.ROOT);

        List<Document> matches = documents.stream()
                .filter(document ->
                        document.title().toLowerCase(Locale.ROOT).contains("payment")
                                || document.content().toLowerCase(Locale.ROOT)
                                .contains("payment")
                )
                .filter(document ->
                        normalized.contains("payment")
                                || normalized.contains("failure")
                                || normalized.contains("authorization")
                                || normalized.contains("incident")
                )
                .toList();

        return matches.isEmpty() ? List.of(documents.getFirst()) : matches;
    }

    public record Document(
            String title,
            String source,
            String content
    ) {}
}