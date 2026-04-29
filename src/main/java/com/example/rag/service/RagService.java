package com.example.rag.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;
import com.example.rag.model.DocumentChunk;
import com.example.rag.client.LlmClient;

@Service
public class RagService {

    private final HybridSearchService searchService;
    private final LlmClient llmClient;

    public RagService(HybridSearchService searchService, @Qualifier("mockLlmClient") LlmClient llmClient) {
        this.searchService = searchService;
        this.llmClient = llmClient;
    }

    public String answer(String question) {

        // 1. Buscar información relevante
        List<DocumentChunk> chunks =
                searchService.search(question, 3);

        // 2. Construir el prompt
        String prompt = buildPrompt(question, chunks);

        // 3. Pedir a la IA que genere la respuesta
        return llmClient.generate(prompt);
    }

    private String buildPrompt(String question,
                               List<DocumentChunk> chunks) {

        StringBuilder context = new StringBuilder();
        for (DocumentChunk chunk : chunks) {
            context.append("- ")
                   .append(chunk.getContent())
                   .append("\n");
        }

        return """
               Usa exclusivamente la información del CONTEXTO.
               Si la respuesta no está en el contexto, di:
               "No dispongo de información suficiente para responder."

               CONTEXTO:
               %s

               PREGUNTA:
               %s
               """.formatted(context, question);
    }
}
