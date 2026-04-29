package com.example.rag.client;

import org.springframework.stereotype.Service;

@Service
public class SimulatedOpenAiEmbeddingClient implements EmbeddingClient {

    private static final int VECTOR_SIZE = 256;

    @Override
    public float[] embed(String text) {
        if (text == null || text.isEmpty()) {
            return new float[VECTOR_SIZE];
        }

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
        // Inicializa la norma del vector a 0
        float norm = 0f;
        // Calcula la suma de los cuadrados de los elementos del vector
        for (float v : vector) {
            norm += v * v;
        }
        // Calcula la raíz cuadrada de la suma para obtener la norma euclidiana
        norm = (float) Math.sqrt(norm);

        // Si la norma es 0, establece todos los elementos del vector a 0 y termina
        if (norm == 0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] = 0.0f;
            }
            return;
        }

        // Divide cada elemento del vector por la norma para normalizarlo
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= norm;
        }
    }
}
