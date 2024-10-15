package com.keiken.pdfTemplateGenerator.Processor;

import com.keiken.pdfTemplateGenerator.Handler.PdfTemplateHandler;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.processor.TemplateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("html")
@RequiredArgsConstructor
@Slf4j
public class PdfTemplateProcessor implements TemplateProcessor {

    private final PdfTemplateHandler templateHandler;

    @Override
    public byte[] processTemplate(String templateFilename, TemplateBaseMapper data) {
        String templateName = templateFilename.split("\\.")[0];

        byte[] pdfBytes = templateHandler.generatePdfLandscape(templateName, data);

        log.info("PDF Template Process Completed");

        return pdfBytes;
    }
}
