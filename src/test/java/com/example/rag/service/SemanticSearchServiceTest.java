package com.example.rag.service;

import com.example.rag.client.EmbeddingClient;
import com.example.rag.model.DocumentChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SemanticSearchServiceTest {

    @Mock
    private DocumentLoader documentLoader;

    @Mock
    private EmbeddingClient embeddingClient;

    private SemanticSearchService semanticSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(documentLoader.loadChunks()).thenReturn(List.of(
                new DocumentChunk("Java is a programming language", "source1.txt"),
                new DocumentChunk("Spring Boot simplifies Java development", "source2.txt")
        ));

        when(embeddingClient.embed("Java is a programming language"))
                .thenReturn(new float[]{1.0f, 0.0f, 0.0f});
        when(embeddingClient.embed("Spring Boot simplifies Java development"))
                .thenReturn(new float[]{0.8f, 0.2f, 0.0f});

        semanticSearchService = new SemanticSearchService(documentLoader, embeddingClient);
    }

    @Test
    void testSearchWithRelevantResults() {
        // Arrange
        String question = "What is Java?";
        when(embeddingClient.embed(question)).thenReturn(new float[]{1.0f, 0.0f, 0.0f});

        // Act
        List<DocumentChunk> results = semanticSearchService.search(question, 2);

        // Assert
        assertEquals(2, results.size());
        assertEquals("Java is a programming language", results.get(0).getContent());
        assertEquals("Spring Boot simplifies Java development", results.get(1).getContent());
    }

    @Test
    void testSearchWithNoRelevantResults() {
        // Arrange
        String question = "What is Python?";
        when(embeddingClient.embed(question)).thenReturn(new float[]{0.0f, 0.0f, 1.0f});

        // Act
        List<DocumentChunk> results = semanticSearchService.search(question, 2);

        // Assert
        assertEquals(2  , results.size());
    }
}