package com.example.rag.client;

public interface EmbeddingClient {
    float[] embed(String text);
}