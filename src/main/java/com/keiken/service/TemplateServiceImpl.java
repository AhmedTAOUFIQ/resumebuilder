package com.keiken.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.pdfTemplateGenerator.Mapper.KeikenTemplateMapper;
import com.keiken.processor.TemplateProcessor;
import com.keiken.strategy.TemplateMapperStrategy;
import com.keiken.strategy.TemplateProcessorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

  private final TemplateMapperStrategy templateMapperStrategy;
  private final TemplateProcessorStrategy templateProcessorStrategy;

  public byte[] processTemplate(String templateFilename, TemplateBaseMapper data) throws IOException {

      TemplateProcessor templateProcessor = templateProcessorStrategy.getProcessor(templateFilename);
      TemplateBaseMapper mapper = templateMapperStrategy.getMapper(templateFilename);

      if(mapper != null) {
          BeanUtils.copyProperties(data, mapper);
          mapper.setProps();
          data = mapper;
      }

      return templateProcessor.processTemplate(templateFilename, data);
  }

    @Override
    public KeikenTemplateMapper loadResumeFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/data.json");
        return objectMapper.readValue(file, KeikenTemplateMapper.class);
    }

}
