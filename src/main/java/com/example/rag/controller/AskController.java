package com.example.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rag.service.RagService;

@RestController
public class AskController {

 private final RagService ragService;

    public AskController(RagService ragService) {
        this.ragService = ragService;
    }


    
    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return ragService.answer(question);
    }

    @GetMapping("/status")
    public String status() {
        return "Application is up and running";
    }
}