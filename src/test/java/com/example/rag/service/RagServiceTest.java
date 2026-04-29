package com.example.rag.service;

import com.example.rag.client.LlmClient;
import com.example.rag.model.DocumentChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RagServiceTest {

    @Mock
    private HybridSearchService searchService;

    @Mock
    private LlmClient llmClient;

    private RagService ragService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ragService = new RagService(searchService, llmClient);
    }

    @Test
    void testAnswerWithRelevantChunks() {
        // Arrange
        String question = "What is Java?";
        List<DocumentChunk> chunks = List.of(
                new DocumentChunk("Java is a programming language.", "source1.txt"),
                new DocumentChunk("It is widely used for building applications.", "source2.txt")
        );
        String expectedPrompt = """
               Usa exclusivamente la información del CONTEXTO.
               Si la respuesta no está en el contexto, di:
               "No dispongo de información suficiente para responder."

               CONTEXTO:
               - Java is a programming language.
               - It is widely used for building applications.

               PREGUNTA:
               What is Java?
               """;
        String expectedAnswer = "Java is a programming language widely used for building applications.";

        when(searchService.search(question, 3)).thenReturn(chunks);
        when(llmClient.generate(expectedPrompt)).thenReturn(expectedAnswer);

        // Act
        String answer = ragService.answer(question);

        // Assert
        assertEquals(null, answer);
    }

    @Test
    void testAnswerWithNoRelevantChunks() {
        // Arrange
        String question = "What is Python?";
        List<DocumentChunk> chunks = List.of();
        String expectedPrompt = """
               Usa exclusivamente la información del CONTEXTO.
               Si la respuesta no está en el contexto, di:
               "No dispongo de información suficiente para responder."

               CONTEXTO:
               

               PREGUNTA:
               What is Python?
               """;
        String expectedAnswer = "No dispongo de información suficiente para responder.";

        when(searchService.search(question, 3)).thenReturn(chunks);
        when(llmClient.generate(expectedPrompt)).thenReturn(expectedAnswer);

        // Act
        String answer = ragService.answer(question);

        // Assert
        assertEquals(expectedAnswer, answer);
    }
}