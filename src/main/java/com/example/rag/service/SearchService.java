package com.example.rag.service;

import java.util.List;

import com.example.rag.model.DocumentChunk;

public interface SearchService {
    List<DocumentChunk> search(String question, int maxResults);
}