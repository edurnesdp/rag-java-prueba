package com.example.rag.client;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OpenAiLlmClient implements LlmClient {

    private final String apiKey;
    private final HttpClient httpClient;

    public OpenAiLlmClient(
            @Value("${openai.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String generate(String prompt) {
        try {
            String requestBody = buildRequestBody(prompt);
            HttpRequest request = buildHttpRequest(requestBody);
            HttpResponse<String> response = sendRequest(request);
            return extractAnswer(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Error generating response", e);
        }
    }

    private String buildRequestBody(String prompt) {
        return """
            {
              "model": "gpt-4o-mini",
              "messages": [
                {
                  "role": "user",
                  "content": %s
                }
              ],
              "temperature": 0.2
            }
            """.formatted(escapeJson(prompt));
    }

    private HttpRequest buildHttpRequest(String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String escapeJson(String input) {
        return JSONObject.quote(input);
    }

    private String extractAnswer(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getString("message");
    }
}