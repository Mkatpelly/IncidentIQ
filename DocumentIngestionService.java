package com.acme.intelligence.rag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentIngestionService {

    private final TextChunker textChunker;
    private final EmbeddingService embeddingService;
    private final KnowledgeDocumentRepository repository;

    public DocumentIngestionService(
            TextChunker textChunker,
            EmbeddingService embeddingService,
            KnowledgeDocumentRepository repository
    ) {
        this.textChunker = textChunker;
        this.embeddingService = embeddingService;
        this.repository = repository;
    }

    @Transactional
    public int ingest(DocumentIngestionRequest request) {
        List<String> chunks = textChunker.chunk(request.content());

        for (int index = 0; index < chunks.size(); index++) {
            String chunk = chunks.get(index);

            String chunkTitle = request.title()
                    + " [chunk "
                    + (index + 1)
                    + "/"
                    + chunks.size()
                    + "]";

            List<Float> embedding = embeddingService.embed(chunk);

            repository.save(
                    UUID.randomUUID(),
                    request.tenantId(),
                    chunkTitle,
                    request.sourceUri(),
                    request.documentType(),
                    chunk,
                    sha256(request.tenantId() + "|" + request.sourceUri() + "|" + chunk),
                    embedding
            );
        }

        return chunks.size();
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    value.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder output = new StringBuilder();

            for (byte item : hash) {
                output.append(String.format("%02x", item));
            }

            return output.toString();
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to generate SHA-256 hash.",
                    exception
            );
        }
    }
}