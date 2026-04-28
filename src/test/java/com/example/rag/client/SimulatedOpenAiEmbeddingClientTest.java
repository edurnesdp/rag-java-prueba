package com.example.rag.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SimulatedOpenAiEmbeddingClientTest {

    private SimulatedOpenAiEmbeddingClient simulatedOpenAiEmbeddingClient;

    @BeforeEach
    void setUp() {
        simulatedOpenAiEmbeddingClient = new SimulatedOpenAiEmbeddingClient();
    }

    @Test
    void testEmbedGeneratesNormalizedVector() {
        // Arrange
        String text = "Hello World!";

        // Act
        float[] vector = simulatedOpenAiEmbeddingClient.embed(text);

        // Assert
        float norm = 0f;
        for (float v : vector) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);
        assertArrayEquals(new float[] {1.0f}, new float[] {norm}, 0.0001f, "Vector should be normalized");
    }

    @Test
    void testEmbedHandlesEmptyString() {
        // Arrange
        String text = "";

        // Act
        float[] vector = simulatedOpenAiEmbeddingClient.embed(text);

        // Assert
        for (float v : vector) {
            assertArrayEquals(new float[] {0.0f}, new float[] {v}, 0.0001f, "All vector values should be zero for empty input");
        }
    }
}