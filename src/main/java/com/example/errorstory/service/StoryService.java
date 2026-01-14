package com.example.errorstory.service;

import com.example.errorstory.engine.ErrorParser;
import com.example.errorstory.engine.ParsedError;
import com.example.errorstory.model.ErrorStory;
import com.example.errorstory.model.User;
import com.example.errorstory.repository.ErrorStoryRepository;
import com.example.errorstory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final ErrorStoryRepository storyRepository;
    private final UserRepository userRepository;
    private final ErrorParser errorParser;

    // CREATE (Generate + Save Story)
    public ErrorStory generateStory(String errorText, String difficulty, Long userId) {

        ParsedError parsed = errorParser.parse(errorText);

        String title = "The Tale of " + parsed.getExceptionType();

        String story = "Once upon a time in the land of " + parsed.getClassName() +
                ", a wild " + parsed.getExceptionType() +
                " appeared and caused chaos at scene " + parsed.getLineNumber() + ".";

        String explanation = null;
        if ("INTERMEDIATE".equalsIgnoreCase(difficulty)) {
            explanation = "This happened because something went wrong at line " +
                    parsed.getLineNumber() +
                    ". You should check object initialization, null checks, or boundaries.";
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    // READ ONE
    public ErrorStory getStoryById(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
    }

    // UPDATE (Edit story manually if needed)
    public ErrorStory updateStory(Long id, ErrorStory updated) {
        ErrorStory existing = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));

        existing.setTitle(updated.getTitle());
        existing.setStory(updated.getStory());
        existing.setExplanation(updated.getExplanation());

        return storyRepository.save(existing);
    }

    // DELETE
    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }
}
