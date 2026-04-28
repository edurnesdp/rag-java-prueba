package com.example.rag.model;

public class EmbeddedChunk {

    private final DocumentChunk chunk;
    private final float[] embedding;

    public EmbeddedChunk(DocumentChunk chunk, float[] embedding) {
        this.chunk = chunk;
        this.embedding = embedding;
    }

    public DocumentChunk getChunk() {
        return chunk;
    }

    public float[] getEmbedding() {
        return embedding;
    }
}

