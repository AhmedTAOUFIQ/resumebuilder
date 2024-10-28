package com.keiken.service;

import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.pdfTemplateGenerator.Mapper.KeikenTemplateMapperPPT;

import java.io.IOException;

public interface TemplateService {
    byte[] processTemplate(String templateFilename, TemplateBaseMapper data) throws IOException;
    //TemplateBaseMapper loadResumeFromJson() throws IOException;
    KeikenTemplateMapperPPT loadResumeFromJson() throws IOException;
}
