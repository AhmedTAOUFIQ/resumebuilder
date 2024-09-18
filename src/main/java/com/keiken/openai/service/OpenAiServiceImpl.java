package com.keiken.openai.service;
import com.keiken.config.AppProperties;
import com.keiken.openai.dto.OpenAiRequest;
import com.keiken.openai.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiServiceImpl implements OpenAiService{

    private final AppProperties appProperties;

    private final RestTemplate restTemplate;

    @Override
    public OpenAiResponse summarizeText(String text, int maxWords) {
        OpenAiResponse response = new OpenAiResponse();
        try {
            String prompt = String.format("Summarize the following text in %d words or less:\n\n%s", maxWords, text);
            OpenAiRequest requestBody = new OpenAiRequest(appProperties.getOpenaiApi().getModel(), prompt);
            response = restTemplate.postForObject(appProperties.getOpenaiApi().getUrl(),requestBody , OpenAiResponse.class);
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new RuntimeException("Invalid response from OpenAI API");
            }
        }  catch (Exception e) {
            log.info("Openai API Service: " + e.getMessage());
        }
        return response;
    }

}
