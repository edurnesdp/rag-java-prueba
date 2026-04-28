package com.example.rag.service;

import com.example.rag.model.DocumentChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class KeyWordSearchServiceTest {

    @Mock
    private DocumentLoader documentLoader;

    private KeyWordSearchService keyWordSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        keyWordSearchService = new KeyWordSearchService(documentLoader);
    }

    @Test
    void testSearchWithMatchingKeywords() {
        // Arrange
        DocumentChunk chunk1 = new DocumentChunk("Java is a programming language", "source1.txt");
        DocumentChunk chunk2 = new DocumentChunk("Spring Boot simplifies Java development", "source2.txt");
        DocumentChunk chunk3 = new DocumentChunk("Python is another popular language", "source3.txt");

        when(documentLoader.loadChunks()).thenReturn(List.of(chunk1, chunk2, chunk3));

        // Act
        List<DocumentChunk> results = keyWordSearchService.search("Java development", 2);

        // Assert
        assertEquals(2, results.size());
        assertEquals(chunk2, results.get(0)); // Higher score due to "development"
        assertEquals(chunk1, results.get(1));
    }

    @Test
    void testSearchWithNoMatchingKeywords() {
        // Arrange
        DocumentChunk chunk1 = new DocumentChunk("Java is a programming language", "source1.txt");
        DocumentChunk chunk2 = new DocumentChunk("Spring Boot simplifies Java development", "source2.txt");

        when(documentLoader.loadChunks()).thenReturn(List.of(chunk1, chunk2));

        // Act
        List<DocumentChunk> results = keyWordSearchService.search("Python", 2);

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    void testSearchWithStopWords() {
        // Arrange
        DocumentChunk chunk1 = new DocumentChunk("Java is a programming language", "source1.txt");
        DocumentChunk chunk2 = new DocumentChunk("Spring Boot simplifies Java development", "source2.txt");

        when(documentLoader.loadChunks()).thenReturn(List.of(chunk1, chunk2));

        // Act
        List<DocumentChunk> results = keyWordSearchService.search("the and of", 2);

        // Assert
        assertEquals(0, results.size());
    }
}