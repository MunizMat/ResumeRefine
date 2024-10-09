package com.resumeassist.services.impl;

import com.resumeassist.services.AIService;
import com.resumeassist.utils.Constants;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;

    @Override
    public String getResumeRecommendations(String parsedResumePDF) {
        OpenAiApi openAiApi = new OpenAiApi(openAiKey);
        ChatModel chatModel = new OpenAiChatModel(openAiApi);
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel("gpt-4o-mini")
                .withTemperature(0.4f)
                .withResponseFormat(
                        new OpenAiApi.ChatCompletionRequest.ResponseFormat(
                                OpenAiApi.ChatCompletionRequest.ResponseFormat.Type.JSON_SCHEMA, Constants.OPEN_AI_JSON_SCHEMA))
                .build();

        String promptContents = Constants.RESUME_ANALYSIS_PROMPT + "\n" + parsedResumePDF;

        Prompt prompt = new Prompt(promptContents, options);
        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getContent();
    }
}
