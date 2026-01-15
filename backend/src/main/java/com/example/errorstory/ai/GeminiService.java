package com.example.errorstory.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateStoryFromGemini(String errorText, String difficulty) {

        String prompt = String.format(
                """
            You are an AI that converts programming errors into short, funny stories.
            
            Return ONLY valid JSON in this exact format:
            
            {
              "title": "Short funny error title",
              "story": "5-6 lines funny story about the error",
              "fix": "Simple explanation on how to fix it",
              "example": "Give a small code example ONLY if difficulty is INTERMEDIATE, otherwise leave it empty"
            }
            
            Rules:
            - Keep everything short.
            - The story must be funny.
            - For BEGINNER: simple language, no heavy technical words.
            - For INTERMEDIATE: include an example code.
            - For ADVANCED: fix should include debugging tips and best practices.
            
            Programming Error:
            %s
            
            Difficulty:
            %s
            """,
                errorText, difficulty
        );


        try {
            // Build request in the exact format Gemini expects
            Map<String, Object> request = new HashMap<>();

            Map<String, Object> parts = new HashMap<>();
            parts.put("text", prompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", Arrays.asList(parts));

            request.put("contents", Arrays.asList(content));

            // Add API key as query parameter
            String urlWithKey = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            // Make the API call
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    urlWithKey,
                    entity,
                    Map.class
            );

            // Extract the story text from response
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                return "⚠️ Error: Empty response from Gemini";
            }

            // Check for errors in response
            if (responseBody.containsKey("error")) {
                Map<String, Object> error = (Map<String, Object>) responseBody.get("error");
                String errorMsg = (String) error.get("message");
                return "⚠️ Gemini API Error: " + errorMsg;
            }

            // Extract the generated text
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) responseBody.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                Map<String, Object> contentResult =
                        (Map<String, Object>) candidate.get("content");

                if (contentResult != null) {
                    List<Map<String, Object>> responseParts =
                            (List<Map<String, Object>>) contentResult.get("parts");

                    if (responseParts != null && !responseParts.isEmpty()) {
                        String text = (String) responseParts.get(0).get("text");
                        return text != null ? text : "⚠️ No text in response";
                    }
                }
            }

            return "⚠️ Could not parse Gemini response";

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Full error: " + e.getMessage());
            return "⚠️ Error calling Gemini API: " + e.getMessage();
        }
    }
}