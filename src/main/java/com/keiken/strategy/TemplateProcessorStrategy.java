package com.keiken.strategy;

import com.keiken.processor.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class TemplateProcessorStrategy {
    private final Map<String, TemplateProcessor> processors;

    @Autowired
    public TemplateProcessorStrategy(Map<String, TemplateProcessor> processors) {
        this.processors = processors;
    }

    public TemplateProcessor getProcessor(String templateName) {
        String extension = templateName.split("\\.")[1];
        TemplateProcessor processor = processors.get(extension);
        if(processor == null) {
            throw new IllegalArgumentException("Unknown processor: " + extension);
        }
        return processor;
    }
}
