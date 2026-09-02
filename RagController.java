package com.acme.intelligence.controller;

import com.acme.intelligence.rag.DocumentChunk;
import com.acme.intelligence.rag.DocumentIngestionRequest;
import com.acme.intelligence.rag.DocumentIngestionService;
import com.acme.intelligence.rag.RagSearchRequest;
import com.acme.intelligence.rag.VectorRetrievalService;
import com.acme.intelligence.service.SecurityService;
import com.acme.intelligence.support.Role;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rag")
public class RagController {

    private final DocumentIngestionService ingestionService;
    private final VectorRetrievalService retrievalService;
    private final SecurityService securityService;

    public RagController(
            DocumentIngestionService ingestionService,
            VectorRetrievalService retrievalService,
            SecurityService securityService
    ) {
        this.ingestionService = ingestionService;
        this.retrievalService = retrievalService;
        this.securityService = securityService;
    }

    @PostMapping("/documents")
    public ResponseEntity<?> ingestDocument(
            @RequestHeader("X-User-Role") Role role,
            @Valid @RequestBody DocumentIngestionRequest request
    ) {
        if (!securityService.hasPermission(role, "manage_documents")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",
                            "Insufficient permissions to ingest documents."
                    ));
        }

        int chunksCreated = ingestionService.ingest(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "chunksCreated",
                        chunksCreated,
                        "tenantId",
                        request.tenantId(),
                        "sourceUri",
                        request.sourceUri()
                ));
    }

    @PostMapping("/search")
    public ResponseEntity<List<DocumentChunk>> search(
            @RequestHeader("X-User-Role") Role role,
            @Valid @RequestBody RagSearchRequest request
    ) {
        if (!securityService.hasPermission(role, "investigate")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<DocumentChunk> results = retrievalService.search(
                request.tenantId(),
                request.query(),
                request.topK()
        );

        return ResponseEntity.ok(results);
    }
}
