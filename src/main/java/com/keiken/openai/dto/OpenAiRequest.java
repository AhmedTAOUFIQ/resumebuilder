package com.keiken.openai.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data @Getter @Setter
@AllArgsConstructor @NoArgsConstructor

public class OpenAiRequest {
    private String model;
    private List<Message> messages;


    public OpenAiRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

}
