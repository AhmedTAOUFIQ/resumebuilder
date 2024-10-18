package com.keiken.processor;

import com.keiken.mapper.TemplateBaseMapper;

import java.io.IOException;

public interface TemplateProcessor {
    byte[] processTemplate(String templateFilename, TemplateBaseMapper mapper) throws IOException;
}
