package com.example.rag.client;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OpenAiLlmClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private OpenAiLlmClient openAiLlmClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        openAiLlmClient = new OpenAiLlmClient("test-api-key");
    }

    @Test
    @Disabled
    void testGenerateSuccess() throws Exception {
        // Arrange
        String prompt = "What is Java?";
        String responseBody = new JSONObject()
                .put("choices", new JSONObject[] {
                        new JSONObject().put("message", "Java is a programming language.")
                })
                .toString();

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenAnswer(invocation -> {
                    HttpResponse.BodyHandler<String> handler = invocation.getArgument(1);
                    return httpResponse;
                });
        when(httpResponse.body()).thenReturn(responseBody);

        // Act
        String result = openAiLlmClient.generate(prompt);

        // Assert
        assertEquals("Java is a programming language.", result);
    }

    @Test
    void testGenerateFailure() throws Exception {
        // Arrange
        String prompt = "What is Java?";
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("API error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> openAiLlmClient.generate(prompt));
    }
}