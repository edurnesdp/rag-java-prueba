package com.example.rag.service;

import com.example.rag.model.DocumentChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class HybridSearchServiceTest {

    @Mock
    private SearchService keywordSearchService;

    @Mock
    private SearchService semanticSearchService;

    private HybridSearchService hybridSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hybridSearchService = new HybridSearchService(keywordSearchService, semanticSearchService);
    }

    @Test
    void testSearchCombinesResults() {
        // Arrange
        DocumentChunk keywordResult1 = new DocumentChunk("Keyword Result 1", "Source 1");
        DocumentChunk keywordResult2 = new DocumentChunk("Keyword Result 2", "Source 2");
        DocumentChunk semanticResult1 = new DocumentChunk("Semantic Result 1", "Source 3");

        when(keywordSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of(keywordResult1, keywordResult2));
        when(semanticSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of(semanticResult1));

        // Act
        List<DocumentChunk> results = hybridSearchService.search("test question", 3);

        // Assert
        assertEquals(3, results.size());
        assertEquals(keywordResult1, results.get(0));
        assertEquals(keywordResult2, results.get(1));
        assertEquals(semanticResult1, results.get(2));
    }

    @Test
    void testSearchWithEmptyResults() {
        // Arrange
        when(keywordSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of());
        when(semanticSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of());

        // Act
        List<DocumentChunk> results = hybridSearchService.search("test question", 3);

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    void testSearchWithPartialResults() {
        // Arrange
        DocumentChunk keywordResult = new DocumentChunk("Keyword Result", "Source 1");
        when(keywordSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of(keywordResult));
        when(semanticSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of());

        // Act
        List<DocumentChunk> results = hybridSearchService.search("test question", 3);

        // Assert
        assertEquals(1, results.size());
        assertEquals(keywordResult, results.get(0));
    }

    @Test
    void testSearchWithMaxResultsLimit() {
        // Arrange
        DocumentChunk keywordResult1 = new DocumentChunk("Keyword Result 1", "Source 1");
        DocumentChunk keywordResult2 = new DocumentChunk("Keyword Result 2", "Source 2");
        DocumentChunk semanticResult = new DocumentChunk("Semantic Result", "Source 3");

        when(keywordSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of(keywordResult1, keywordResult2));
        when(semanticSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of(semanticResult));

        // Act
        List<DocumentChunk> results = hybridSearchService.search("test question", 2);

        // Assert
        assertEquals(2, results.size());
        assertEquals(keywordResult1, results.get(0));
        assertEquals(keywordResult2, results.get(1));
    }

    @Test
    void testSearchWithWeightedResults() {
        // Arrange
        DocumentChunk keywordResult = new DocumentChunk("Keyword Result", "Source 1");
        DocumentChunk semanticResult = new DocumentChunk("Semantic Result", "Source 2");

        when(keywordSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of(keywordResult));
        when(semanticSearchService.search(any(String.class), anyInt()))
                .thenReturn(List.of(semanticResult));

        // Act
        List<DocumentChunk> results = hybridSearchService.search("test question", 2);

        // Assert
        assertEquals(2, results.size());
        assertEquals(keywordResult, results.get(0)); // Keyword result should have higher weight
        assertEquals(semanticResult, results.get(1));
    }
}