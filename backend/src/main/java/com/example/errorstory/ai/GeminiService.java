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
                "You are a creative storyteller. Turn this programming error into a %s difficulty story.\n\n" +
                        "Rules:\n" +
                        "- For BEGINNER: Make it simple, fun, and easy to understand like a fairy tale\n" +
                        "- For INTERMEDIATE: Add technical explanation of what caused the error\n" +
                        "- For ADVANCED: Include detailed debugging steps and best practices\n\n" +
                        "Programming Error:\n%s\n\n" +
                        "Create an engaging and creative story:",
                difficulty, errorText
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