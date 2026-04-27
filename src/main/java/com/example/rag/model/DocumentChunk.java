package com.example.rag.model;

public class DocumentChunk {
   

    private final String content;
    private final String source;

    public DocumentChunk(String content, String source) {
        this.content = content;
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }
}

