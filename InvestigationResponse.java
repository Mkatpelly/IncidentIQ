package com.acme.intelligence.dto;

import java.util.List;
import java.util.Map;

public record InvestigationResponse(
        String diagnosis,
        List<EvidenceItem> evidence,
        List<String> toolsUsed,
        Recommendation recommendation,
        Map<String, Object> raw
) {

    public record EvidenceItem(
            String source,
            String detail
    ) {}

    public record Recommendation(
            String summary,
            double confidence,
            String risk,
            boolean approvalRequired
    ) {}
}