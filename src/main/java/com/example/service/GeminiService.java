package com.example.service;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeminiService {
    private static final String GEMINI_API_URL = "http://localhost:3000/generate";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAIResponse(String prompt) throws Exception {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty.");
        }

        System.out.println("Received prompt: " + prompt);

        String payload = objectMapper.writeValueAsString(new PromptRequest(prompt));
        System.out.println("Prepared payload: " + payload);

        try {
            String response = Request.post(GEMINI_API_URL)
                    .bodyString(payload, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();

            System.out.println("Received raw response: " + response);

            PromptResponse promptResponse = objectMapper.readValue(response, PromptResponse.class);
            System.out.println("Parsed AI response: " + promptResponse.getResponse());
            return promptResponse.getResponse();
        } catch (Exception e) {
            System.err.println("Error during API communication: " + e.getMessage());
            throw e; // Rethrow the exception for proper handling in UI
        }
    }

    private static class PromptRequest {
        private final String prompt;

        public PromptRequest(String prompt) {
            this.prompt = prompt;
        }

        public String getPrompt() {
            return prompt;
        }
    }

    private static class PromptResponse {
        private String response;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}
