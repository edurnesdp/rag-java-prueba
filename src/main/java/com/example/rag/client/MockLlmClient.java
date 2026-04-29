package com.example.rag.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {

        System.out.println("Contenido del prompt: " + prompt);

    

        return prompt;
    }
}
