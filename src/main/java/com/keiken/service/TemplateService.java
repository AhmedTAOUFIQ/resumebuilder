package com.keiken.service;

import com.keiken.mapper.TemplateBaseMapper;

public interface TemplateService {
    byte[] processTemplate(String templateFilename, TemplateBaseMapper data);
}
