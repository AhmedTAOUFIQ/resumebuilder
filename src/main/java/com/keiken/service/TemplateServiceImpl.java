package com.keiken.service;

import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.processor.TemplateProcessor;
import com.keiken.strategy.TemplateMapperStrategy;
import com.keiken.strategy.TemplateProcessorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

  private final TemplateMapperStrategy templateMapperStrategy;
  private final TemplateProcessorStrategy templateProcessorStrategy;

  public byte[] processTemplate(String templateFilename, TemplateBaseMapper data) {

      TemplateProcessor templateProcessor = templateProcessorStrategy.getProcessor(templateFilename);
      TemplateBaseMapper mapper = templateMapperStrategy.getMapper(templateFilename);

      if(mapper != null) {
          BeanUtils.copyProperties(data, mapper);
          // Set additional properties for specific template placeholders
          mapper.setProps();
          data = mapper;
      }

      return templateProcessor.processTemplate(templateFilename, data);
  }

}
