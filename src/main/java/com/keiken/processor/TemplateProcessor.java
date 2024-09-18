package com.keiken.processor;

import com.keiken.mapper.TemplateBaseMapper;

public interface TemplateProcessor {
    byte[] processTemplate(String templateFilename, TemplateBaseMapper mapper);
}
