package com.example.rag.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("semantic")
public class SimulatedOpenAiEmbeddingClient implements EmbeddingClient {

    private static final int VECTOR_SIZE = 256;

    @Override
    public float[] embed(String text) {
        float[] vector = new float[VECTOR_SIZE];
        String normalized = normalize(text);

        for (String token : normalized.split("\\s+")) {
            int hash = token.hashCode();
            int index = Math.abs(hash % VECTOR_SIZE);
            vector[index] += 1.0f;
        }

        normalizeVector(vector);
        return vector;
    }

    private String normalize(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-záéíóúñ ]", "");
    }

    private void normalizeVector(float[] vector) {
        float norm = 0f;
        for (float v : vector) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);

        if (norm == 0) return;

        for (int i = 0; i < vector.length; i++) {
            vector[i] /= norm;
        }
    }
}
