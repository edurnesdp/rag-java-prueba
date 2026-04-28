package com.example.rag.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.rag.model.EmbeddedChunk;
import com.example.rag.model.DocumentChunk;
import com.example.rag.client.EmbeddingClient;

@Service
@Profile("semantic")
public class SemanticSearchService implements SearchService {

    private final List<EmbeddedChunk> embeddedChunks;
    private final EmbeddingClient embeddingClient;

    public SemanticSearchService(
            DocumentLoader loader,
            EmbeddingClient embeddingClient) {

        this.embeddingClient = embeddingClient;

        this.embeddedChunks = loader.loadChunks()
            .stream()
            .map(chunk ->
                new EmbeddedChunk(
                    chunk,
                    embeddingClient.embed(chunk.getContent())
                )
            )
            .toList();
    }

    @Override
    public List<DocumentChunk> search(String question, int maxResults) {

        float[] questionEmbedding =
            embeddingClient.embed(question);

        return embeddedChunks.stream()
            .map(ec -> new ScoredChunk(
                ec.getChunk(),
                similarity(questionEmbedding, ec.getEmbedding())
            ))
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(maxResults)
            .map(sc -> sc.chunk)
            .toList();
    }

    private double similarity(float[] v1, float[] v2) {
        // cosine similarity simplificada
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;

        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private record ScoredChunk(
        DocumentChunk chunk,
        double score
    ) {}
}

