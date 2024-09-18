package com.keiken.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.application")
@Getter @Setter
public class AppProperties {

    private String name;
    private Templates templates;
    private OpenaiApi openaiApi;

    @Getter @Setter
    public static class Templates {
        private String pptTemplatesClasspath;
        private String pptOutputPath;
        private String pdfTemplatesClasspath;
        private String pdfOutputPath;
        private String imagesClasspath;
    }

    @Getter @Setter
    public static class OpenaiApi {
        private String url;
        private String key;
        private String model;
    }

}