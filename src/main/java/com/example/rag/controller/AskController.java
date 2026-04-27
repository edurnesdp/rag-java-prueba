package com.example.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        // Placeholder logic for handling the question
        return "You asked: " + question;
    }

    @GetMapping("/status")
    public String status() {
        return "Application is up and running";
    }
}