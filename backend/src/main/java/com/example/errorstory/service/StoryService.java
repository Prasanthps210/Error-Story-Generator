package com.example.errorstory.service;

import com.example.errorstory.ai.GeminiService;
import com.example.errorstory.ai.InternetChecker;
import com.example.errorstory.engine.ErrorParser;
import com.example.errorstory.engine.OfflineStoryEngine;
import com.example.errorstory.engine.ParsedError;
import com.example.errorstory.model.ErrorStory;
import com.example.errorstory.model.User;
import com.example.errorstory.repository.ErrorStoryRepository;
import com.example.errorstory.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StoryService {

    private final ErrorStoryRepository storyRepository;
    private final UserRepository userRepository;
    private final ErrorParser errorParser;
    private final GeminiService geminiService;
    private final InternetChecker internetChecker;
    private final OfflineStoryEngine offlineStoryEngine;


    public StoryService(
            ErrorStoryRepository storyRepository,
            UserRepository userRepository,
            ErrorParser errorParser,
            GeminiService geminiService,
            InternetChecker internetChecker,
            OfflineStoryEngine offlineStoryEngine
    ) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.errorParser = errorParser;
        this.geminiService = geminiService;
        this.internetChecker = internetChecker;
        this.offlineStoryEngine = offlineStoryEngine;
    }

    // CREATE → Generate story
    public ErrorStory generateStory(String errorText, String difficulty, Long userId, String mode) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String title;
        String story;
        String explanation = null;
        String example="";

        boolean useAI = "AI".equalsIgnoreCase(mode) && internetChecker.isInternetAvailable();

        if (useAI) {
            // 🌐 AI Mode (Gemini)

            String aiResponse = geminiService.generateStoryFromGemini(errorText, difficulty);

            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> result = mapper.readValue(aiResponse, Map.class);

                title = result.getOrDefault("title", "AI Story");
                story = result.getOrDefault("story", aiResponse);
                explanation = result.getOrDefault("fix", "FIX: Not provided by AI");

                example = result.getOrDefault("example", "");

                System.out.println("Example Code:\n" + example);

            } catch (Exception e) {
                // If Gemini returns broken JSON
                title = "AI Error";
                story = aiResponse;
                explanation = "Failed to parse structured response";
                e.printStackTrace();
            }

        } else {
            ParsedError parsed = errorParser.parse(errorText);

            var offline = offlineStoryEngine.generate(difficulty, parsed);

            title = offline.getTitle();
            story = offline.getStory();
            explanation = offline.getExplanation();
            example = offline.getExample();
        }

        ErrorStory errorStory = new ErrorStory();
        errorStory.setTitle(title);
        errorStory.setStory(story);
        errorStory.setExplanation(explanation);
        errorStory.setExample(example);
        errorStory.setUser(user);

        return storyRepository.save(errorStory);
    }

    // READ → History (Library)
    public List<ErrorStory> getHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return storyRepository.findByUserOrderByIdDesc(user);
    }

    // READ ONE
    public ErrorStory getStoryById(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
    }

    // UPDATE
    public ErrorStory updateStory(Long id, ErrorStory updated) {
        ErrorStory existing = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));

        existing.setTitle(updated.getTitle());
        existing.setStory(updated.getStory());
        existing.setExplanation(updated.getExplanation());
        existing.setExample(updated.getExample());

        return storyRepository.save(existing);
    }

    // DELETE
    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }
}
