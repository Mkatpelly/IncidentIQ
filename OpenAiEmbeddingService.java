package com.acme.intelligence.rag;

import com.acme.intelligence.config.EmbeddingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@ConditionalOnProperty(
        prefix = "incidentiq.embeddings",
        name = "provider",
        havingValue = "openai"
)
public class OpenAiEmbeddingService implements EmbeddingService {

    private final RestClient restClient;
    private final EmbeddingProperties properties;

    public OpenAiEmbeddingService(
            RestClient.Builder restClientBuilder,
            EmbeddingProperties properties
    ) {
        this.properties = properties;

        this.restClient = restClientBuilder
                .baseUrl(properties.getApiUrl())
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + properties.getApiKey()
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }

    @Override
    public List<Float> embed(String text) {
        EmbeddingRequest request = new EmbeddingRequest(
                properties.getModel(),
                text
        );

        EmbeddingResponse response = restClient.post()
                .body(request)
                .retrieve()
                .body(EmbeddingResponse.class);

        if (response == null
                || response.data() == null
                || response.data().isEmpty()) {
            throw new IllegalStateException(
                    "Embedding API returned no embedding data."
            );
        }

        List<Float> embedding = response.data().getFirst().embedding();

        if (embedding.size() != properties.getDimensions()) {
            throw new IllegalStateException(
                    "Embedding dimension mismatch. Expected "
                            + properties.getDimensions()
                            + " but received "
                            + embedding.size()
            );
        }

        return embedding;
    }

    private record EmbeddingRequest(
            String model,
            String input
    ) {
    }

    private record EmbeddingResponse(
            List<EmbeddingData> data
    ) {
    }

    private record EmbeddingData(
            List<Float> embedding,
            int index
    ) {
    }
}
