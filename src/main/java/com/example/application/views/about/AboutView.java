package com.example.application.views.about;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.example.service.GeminiService;

@Route("")
public class AboutView extends VerticalLayout {
    private final GeminiService geminiService = new GeminiService();

    public AboutView() {
        TextField inputField = new TextField("Enter your prompt");
        TextArea responseTextArea = new TextArea("AI Response");
        responseTextArea.setReadOnly(true);

        Button generateButton = new Button("Generate Response", event -> {
            String prompt = inputField.getValue();

            if (prompt == null || prompt.trim().isEmpty()) {
                responseTextArea.setValue("Please enter a valid prompt.");
                return;
            }

            try {
                String response = geminiService.getAIResponse(prompt);
                responseTextArea.setValue(response);
            } catch (IllegalArgumentException e) {
                responseTextArea.setValue("Invalid input: " + e.getMessage());
            } catch (Exception e) {
                responseTextArea.setValue("An error occurred while communicating with the API.");
                e.printStackTrace(); // Logs the error for debugging
            }
        });

        add(inputField, generateButton, responseTextArea);
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
    }
}
