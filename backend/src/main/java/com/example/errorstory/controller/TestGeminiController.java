package com.example.errorstory.controller;

import com.example.errorstory.ai.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestGeminiController {

    private final GeminiService geminiService;

    public TestGeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/gemini")
    public String testGemini() {
        String testError = "NullPointerException at line 42 in Main.java";
        return geminiService.generateStoryFromGemini(testError, "BEGINNER");
    }
    @GetMapping("/ping")
    public String ping() {
        return "Server is running! ✅";
    }
}

