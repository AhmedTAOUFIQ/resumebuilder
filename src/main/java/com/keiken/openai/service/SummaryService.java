package com.keiken.openai.service;

import com.keiken.openai.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final OpenAiService openAiService;

    @Cacheable(cacheNames = "summaries", key = "#text")
    public String getSummary(String text, int maxWords) {
        OpenAiResponse response = openAiService.summarizeText(text, maxWords);

        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }

        return text;
    }
}