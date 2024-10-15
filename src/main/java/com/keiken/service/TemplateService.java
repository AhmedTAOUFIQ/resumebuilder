package com.keiken.service;

import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.pdfTemplateGenerator.Mapper.KeikenTemplateMapper;

import java.io.IOException;

public interface TemplateService {
    byte[] processTemplate(String templateFilename, TemplateBaseMapper data);
    //TemplateBaseMapper loadResumeFromJson() throws IOException;
    KeikenTemplateMapper loadResumeFromJson() throws IOException;
}
