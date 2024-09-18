package com.keiken.dto;

import com.keiken.mapper.TemplateBaseMapper;

public record TemplateRequest(
        String templateName,
        TemplateBaseMapper data
) {}