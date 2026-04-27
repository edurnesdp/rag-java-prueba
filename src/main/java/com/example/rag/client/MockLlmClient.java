package com.example.rag.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {

        if (prompt.contains("No dispongo de información suficiente")) {
            return "No dispongo de información suficiente para responder.";
        }

        return """
               [RESPUESTA GENERADA POR MockLlmClient]

               Esta respuesta se ha generado utilizando únicamente
               la información proporcionada en el contexto.

               (Simulación de respuesta de una IA)
               """;
    }
}
