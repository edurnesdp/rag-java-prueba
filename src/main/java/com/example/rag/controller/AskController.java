package com.example.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rag.service.RagService;
import com.example.rag.dto.QuestionRequest;

@RestController
public class AskController {

 private final RagService ragService;

    public AskController(RagService ragService) {
        this.ragService = ragService;
    }


    
    @PostMapping("/ask")
    public String ask(@RequestBody QuestionRequest questionRequest) {
        return ragService.answer(questionRequest.getQuestion());
    }

    @GetMapping("/status")
    public String status() {
        return "Application is up and running";
    }
}