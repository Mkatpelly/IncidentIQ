package com.acme.intelligence.rag;

import com.acme.intelligence.config.RagProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorRetrievalService {

    private final EmbeddingService embeddingService;
    private final KnowledgeDocumentRepository repository;
    private final RagProperties ragProperties;

    public VectorRetrievalService(
            EmbeddingService embeddingService,
            KnowledgeDocumentRepository repository,
            RagProperties ragProperties
    ) {
        this.embeddingService = embeddingService;
        this.repository = repository;
        this.ragProperties = ragProperties;
    }

    public List<DocumentChunk> search(
            String tenantId,
            String query,
            Integer requestedTopK
    ) {
        int topK = requestedTopK == null
                ? ragProperties.getDefaultTopK()
                : requestedTopK;

        List<Float> queryEmbedding = embeddingService.embed(query);

        return repository.semanticSearch(
                        tenantId,
                        queryEmbedding,
                        topK
                )
                .stream()
                .filter(
                        chunk -> chunk.similarity()
                                >= ragProperties.getMinSimilarity()
                )
                .toList();
    }
}
