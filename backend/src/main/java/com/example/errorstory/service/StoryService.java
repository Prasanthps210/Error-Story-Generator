package com.example.errorstory.service;

import com.example.errorstory.ai.GeminiService;
import com.example.errorstory.ai.InternetChecker;
import com.example.errorstory.engine.ErrorParser;
import com.example.errorstory.engine.ParsedError;
import com.example.errorstory.model.ErrorStory;
import com.example.errorstory.model.User;
import com.example.errorstory.repository.ErrorStoryRepository;
import com.example.errorstory.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import java.util.List;

@Service
public class StoryService {

    private final ErrorStoryRepository storyRepository;
    private final UserRepository userRepository;
    private final ErrorParser errorParser;
    private final GeminiService geminiService;
    private final InternetChecker internetChecker;

    public StoryService(
            ErrorStoryRepository storyRepository,
            UserRepository userRepository,
            ErrorParser errorParser,
            GeminiService geminiService,
            InternetChecker internetChecker
    ) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.errorParser = errorParser;
        this.geminiService = geminiService;
        this.internetChecker = internetChecker;
    }

    public ErrorStory generateStory(String errorText, String difficulty, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String title;
        String story;
        String explanation = null;

        if (internetChecker.isInternetAvailable()) {
            // 🌐 Gemini mode (Structured + Funny)

            String aiResponse = geminiService.generateStoryFromGemini(errorText, difficulty);

            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> result = mapper.readValue(aiResponse, Map.class);

                title = result.get("title");
                story = result.get("story");
                explanation = result.get("fix");

                String example = result.get("example");
                System.out.println("Example Code: " + example);

            } catch (Exception e) {
                // Fallback if Gemini returns bad JSON
                title = "AI Error";
                story = aiResponse;
                explanation = "Failed to parse structured response";
                e.printStackTrace();
            }

        } else {

            // 🧠 Offline mode
            ParsedError parsed = errorParser.parse(errorText);
            title = "The Tale of " + parsed.getExceptionType();
            story = "Once upon a time in the land of " + parsed.getClassName()
                    + ", a wild " + parsed.getExceptionType()
                    + " appeared at line " + parsed.getLineNumber() + ".";

            if ("INTERMEDIATE".equalsIgnoreCase(difficulty)) {
                explanation = "Check null references, array bounds, and object initialization near line "
                        + parsed.getLineNumber();
            }
        }

        ErrorStory errorStory = new ErrorStory();
        errorStory.setTitle(title);
        errorStory.setStory(story);
        errorStory.setExplanation(explanation);
        errorStory.setUser(user);

        return storyRepository.save(errorStory);
    }

    // READ (History / Bookshelf)
    public List<ErrorStory> getHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return storyRepository.findByUser(user);
    }

    // READ ONE STORY
    public ErrorStory getStoryById(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
    }

    // UPDATE STORY
    public ErrorStory updateStory(Long id, ErrorStory updated) {
        ErrorStory existing = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));

        existing.setTitle(updated.getTitle());
        existing.setStory(updated.getStory());
        existing.setExplanation(updated.getExplanation());

        return storyRepository.save(existing);
    }

    // DELETE STORY
    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }
}
