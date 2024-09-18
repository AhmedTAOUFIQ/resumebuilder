package com.keiken.openai.service;

import com.keiken.openai.dto.OpenAiResponse;

public interface OpenAiService {
    OpenAiResponse summarizeText(String text, int maxWords );
}
