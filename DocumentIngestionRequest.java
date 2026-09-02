package com.acme.intelligence.rag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentIngestionRequest(
        @NotBlank String tenantId,
        @NotBlank String title,
        @NotBlank String sourceUri,
        @NotNull DocumentType documentType,
        @NotBlank String content
) {
}
