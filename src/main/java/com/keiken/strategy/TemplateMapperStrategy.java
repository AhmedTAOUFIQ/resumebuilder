package com.keiken.strategy;

import com.keiken.mapper.TemplateBaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class TemplateMapperStrategy {
    private final Map<String, TemplateBaseMapper> mappers;

    @Autowired
    public TemplateMapperStrategy(Map<String, TemplateBaseMapper> mappers) {
        this.mappers = mappers;
    }

    public TemplateBaseMapper getMapper(String templateName) {
        TemplateBaseMapper mapper = mappers.get(templateName);
        if(mapper == null) {
            log.info("Unknown Mapper: " + templateName);
        }
        return mapper;
    }
}
