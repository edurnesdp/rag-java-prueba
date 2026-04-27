package com.example.rag.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.example.rag.model.DocumentChunk;

@Service
public class DocumentLoader {

    private static final String DOCS_PATH = "docs";

    public List<DocumentChunk> loadChunks() {
        List<DocumentChunk> chunks = new ArrayList<>();

        try {
            Files.list(Path.of(DOCS_PATH))
                 .filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".txt"))
                 .forEach(path -> loadFile(path, chunks));
        } catch (IOException e) {
            throw new RuntimeException("Error loading documents", e);
        }

        return chunks;
    }

    private void loadFile(Path path, List<DocumentChunk> chunks) {
        try {
            String content = Files.readString(path);
            String source = path.getFileName().toString();

            String[] paragraphs = content.split("\\R\\R+");

            for (String paragraph : paragraphs) {
                String trimmed = paragraph.trim();
                if (!trimmed.isEmpty()) {
                    chunks.add(new DocumentChunk(trimmed, source));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + path, e);
        }
    }
}