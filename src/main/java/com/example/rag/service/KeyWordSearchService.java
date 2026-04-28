package com.example.rag.service;

import com.example.rag.model.DocumentChunk;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service("keywordSearchService")
public class KeyWordSearchService implements SearchService {

    private final List<DocumentChunk> chunks;

    private static final Set<String> STOP_WORDS = Set.of(
    "el", "la", "los", "las",
    "de", "en", "y", "o",
    "como", "que", "para",
    "un", "una", "es"
);


    public KeyWordSearchService(DocumentLoader documentLoader) {
        this.chunks = documentLoader.loadChunks();
    }

    @Override
    public List<DocumentChunk> search(String question, int maxResults) {

        String[] keywords = extractKeywords(question).toArray(new String[0]);

        return chunks.stream()
                .map(chunk -> new ScoredChunk(chunk, score(chunk, keywords)))
                .filter(sc -> sc.score > 0)
                .sorted((a, b) -> Integer.compare(b.score, a.score))
                .limit(maxResults)
                .map(sc -> sc.chunk)
                .toList();
    }

    private int score(DocumentChunk chunk, String[] keywords) {
        int score = 0;
        String text = chunk.getContent().toLowerCase();

        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                score++;
            }
        }
        return score;
    }

    private List<String> extractKeywords(String question) {
        return Arrays.stream(question.toLowerCase().split("\\s+"))
            .map(word -> word.replaceAll("[^a-záéíóúñ]", ""))
            .filter(word -> !word.isBlank())
            .filter(word -> !STOP_WORDS.contains(word))
            .toList();
}

    private record ScoredChunk(DocumentChunk chunk, int score) {}
}