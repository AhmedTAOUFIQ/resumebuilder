package com.keiken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keiken.config.AppProperties;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.service.TemplateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(AppProperties.class)
public class PowerpointtemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerpointtemplateApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TemplateService templateService) {
		return (args) -> {
			ObjectMapper objectMapper = new ObjectMapper();
			File jsonFile = new ClassPathResource("data.json").getFile();
			TemplateBaseMapper templateData = objectMapper.readValue(jsonFile, TemplateBaseMapper.class);

			templateService.processTemplate("pdfHtmlTemplate.html", templateData);
		};
	}
}
