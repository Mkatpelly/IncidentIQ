package com.acme.intelligence.rag;

import com.acme.intelligence.config.EmbeddingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(
        prefix = "incidentiq.embeddings",
        name = "provider",
        havingValue = "deterministic"
)
public class DeterministicEmbeddingService implements EmbeddingService {

    private final int dimensions;

    public DeterministicEmbeddingService(EmbeddingProperties properties) {
        this.dimensions = properties.getDimensions();
    }

    @Override
    public List<Float> embed(String text) {
        float[] vector = new float[dimensions];
        String normalized = text.toLowerCase();

        for (int i = 0; i < normalized.length(); i++) {
            int index = Math.floorMod(
                    normalized.charAt(i) * 31 + i,
                    dimensions
            );

            vector[index] += 1.0f;
        }

        double norm = 0.0;

        for (float value : vector) {
            norm += value * value;
        }

        norm = Math.sqrt(norm);

        List<Float> output = new ArrayList<>(dimensions);

        for (float value : vector) {
            output.add(norm == 0.0 ? 0.0f : (float) (value / norm));
        }

        return output;
    }
}
