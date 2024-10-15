package com.keiken.pdfTemplateGenerator.Handler;

import com.keiken.mapper.TemplateBaseMapper;

public interface PdfTemplateHandler {
    byte[] savePdfToFile(byte[] pdfBytes, String fileName);

    byte[] generatePdf(String templateName, TemplateBaseMapper templateBaseMapper);

    byte[] generatePdfLandscape(String templateName, TemplateBaseMapper templateBaseMapper);
}
