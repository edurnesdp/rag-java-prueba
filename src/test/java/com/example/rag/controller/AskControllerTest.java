package com.example.rag.controller;

import com.example.rag.service.RagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AskController.class)
class AskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RagService ragService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @WithMockUser(username = "testUser", roles = {"USER"})
    @Test
    void testAskEndpoint() throws Exception {
        // Arrange
        String question = "What is the capital of France?";
        String answer = "The capital of France is Paris.";
        when(ragService.answer(question)).thenReturn(answer);

        // Act & Assert
        mockMvc.perform(get("/ask").param("question", question))
                .andExpect(status().isOk())
                .andExpect(content().string(answer));
    }

    @WithMockUser(username = "testUser", roles = {"USER"})
    @Test
    void testStatusEndpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application is up and running"));
    }
}