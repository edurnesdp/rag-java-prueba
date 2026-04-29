package com.example.rag.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.example.rag.model.DocumentChunk;

@Service
public class HybridSearchService implements SearchService {

    private final SearchService keywordSearchService;
    private final SearchService semanticSearchService;

    public HybridSearchService(
            @Qualifier("keywordSearchService") SearchService keywordSearchService,
            @Qualifier("semanticSearchService") SearchService semanticSearchService) {
        this.keywordSearchService = keywordSearchService;
        this.semanticSearchService = semanticSearchService;
    }

    @Override
    public List<DocumentChunk> search(String question, int maxResults) {

        // 1. Ejecutar ambas búsquedas
        List<DocumentChunk> keywordResults =
                keywordSearchService.search(question, maxResults);

        List<DocumentChunk> semanticResults =
                semanticSearchService.search(question, maxResults);
        
        System.out.println("Keyword results: " + keywordResults.size());
        System.out.println("Semantic results: " + semanticResults.size());

        // 2. Combinar resultados con puntuación
        Map<DocumentChunk, Double> scores = new HashMap<>();

        // Keywords → más peso (precisión)
        for (int i = 0; i < keywordResults.size(); i++) {
            scores.merge(
                keywordResults.get(i),
                2.0 - (i * 0.1),
                Double::sum
            );
        }

        // Semántica → menos peso (flexibilidad)
        for (int i = 0; i < semanticResults.size(); i++) {
            scores.merge(
                semanticResults.get(i),
                1.0 - (i * 0.1),
                Double::sum
            );
        }

        // 3. Ordenar y devolver los mejores
        return scores.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(maxResults)
                .map(Map.Entry::getKey)
                .toList();
    }
}
