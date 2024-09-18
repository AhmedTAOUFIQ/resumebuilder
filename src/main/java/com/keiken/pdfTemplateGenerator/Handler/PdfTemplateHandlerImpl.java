package com.keiken.pdfTemplateGenerator.Handler;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.keiken.config.AppProperties;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.openai.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PdfTemplateHandlerImpl implements PdfTemplateHandler{

    private final AppProperties appProperties;

    private final TemplateEngine templateEngine;
    private final SummaryService summaryService;

    @Override
    public byte[] savePdfToFile(byte[] pdfBytes, String filename) {
        try {
            Path outputFilePath = Paths.get(appProperties.getTemplates().getPdfOutputPath(), filename + ".pdf");

            if (Files.notExists(outputFilePath.getParent())) {
                Files.createDirectories(outputFilePath.getParent());
            }

            try (OutputStream outputStream = new FileOutputStream(outputFilePath.toFile())) {
                outputStream.write(pdfBytes);
                return pdfBytes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public byte[] generatePdf(String templateName, TemplateBaseMapper templateBaseMapper) {
        Context context = new Context();
        context.setVariable("data", templateBaseMapper);
        String htmlTemplate = templateEngine.process(
                appProperties.getTemplates().getPdfTemplatesClasspath() + templateName, context
        );

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        HtmlConverter.convertToPdf(htmlTemplate, target, converterProperties);
        byte[] pdfBytes = target.toByteArray();

        return savePdfToFile(pdfBytes, UUID.randomUUID().toString());
    }

}
