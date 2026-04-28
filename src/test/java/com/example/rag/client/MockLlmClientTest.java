package com.example.rag.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MockLlmClientTest {

    private MockLlmClient mockLlmClient;

    @BeforeEach
    void setUp() {
        mockLlmClient = new MockLlmClient();
    }

    @Test
    void testGenerateWithInsufficientInformation() {
        // Arrange
        String prompt = "No dispongo de información suficiente";

        // Act
        String result = mockLlmClient.generate(prompt);

        // Assert
        assertEquals("No dispongo de información suficiente para responder.", result);
    }

    @Test
    void testGenerateWithValidPrompt() {
        // Arrange
        String prompt = "Contexto válido para generar una respuesta.";

        // Act
        String result = mockLlmClient.generate(prompt);

        // Assert
        String expected = """
               [RESPUESTA GENERADA POR MockLlmClient]

               Esta respuesta se ha generado utilizando únicamente
               la información proporcionada en el contexto.

               (Simulación de respuesta de una IA)
               """;
        assertEquals(expected, result);
    }
}