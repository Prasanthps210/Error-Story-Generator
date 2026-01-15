package com.example.errorstory.controller;

import com.example.errorstory.model.ErrorStory;
import com.example.errorstory.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/story")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StoryController {

    private final StoryService storyService;

    // CREATE → Generate story from error + save to DB
    @PostMapping("/generate/{userId}")
    public ErrorStory generateStory(
            @PathVariable Long userId,
            @RequestBody Map<String, String> data) {

        String errorText = data.get("errorText");
        String difficulty = data.get("difficulty");
        String mode = data.get("mode");  // AI or DEFAULT

        return storyService.generateStory(errorText, difficulty, userId, mode);
    }


    // READ → Get all stories (Bookshelf) for user
    @GetMapping("/history/{userId}")
    public List<ErrorStory> getHistory(@PathVariable Long userId) {
        return storyService.getHistory(userId);
    }

    // READ ONE STORY
    @GetMapping("/{id}")
    public ErrorStory getStoryById(@PathVariable Long id) {
        return storyService.getStoryById(id);
    }

    // UPDATE STORY
    @PutMapping("/update/{id}")
    public ErrorStory updateStory(
            @PathVariable Long id,
            @RequestBody ErrorStory updatedStory) {

        return storyService.updateStory(id, updatedStory);
    }

    // DELETE STORY
    @DeleteMapping("/delete/{id}")
    public String deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return "Story deleted successfully";
    }
}
