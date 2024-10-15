package com.keiken.pdfTemplateGenerator.Handler;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.keiken.config.AppProperties;
import com.keiken.mapper.TemplateBaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PdfTemplateHandlerImpl implements PdfTemplateHandler {

    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @Override
    public byte[] savePdfToFile(byte[] pdfBytes, String filename) {
        try {
            Path outputFilePath = Paths.get(appProperties.getTemplates().getPdfOutputPath(), filename + ".pdf");

            if (Files.notExists(outputFilePath.getParent())) {
                Files.createDirectories(outputFilePath.getParent());
            }

            try (OutputStream outputStream = Files.newOutputStream(outputFilePath)) {
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

        // 1. Generate the HTML template content
        String htmlTemplate = templateEngine.process(appProperties.getTemplates().getPdfTemplatesClasspath() + templateName, context);

        // 2. Prepare a ByteArrayOutputStream to hold the PDF data
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        // 3. Prepare PdfWriter and PdfDocument
        PdfWriter writer = new PdfWriter(target);
        PdfDocument pdfDocument = new PdfDocument(writer);

        // 4. Prepare Document object
        Document document = new Document(pdfDocument, PageSize.A4);

        // Set margins (remove or customize them as needed)
        document.setMargins(0, 0, 0, 0);

        // 5. Prepare ConverterProperties
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080"); // Adjust as per your app

        // 6. Convert the HTML content into the PDF document
        HtmlConverter.convertToPdf(htmlTemplate, pdfDocument, converterProperties);

        // Close the document to complete the PDF generation
        document.close();

        // 7. Return the generated PDF as byte array
        byte[] pdfBytes = target.toByteArray();
        return savePdfToFile(pdfBytes, UUID.randomUUID().toString());
    }

    @Override
    public byte[] generatePdfLandscape(String templateName, TemplateBaseMapper templateBaseMapper) {
        Context context = new Context();
        context.setVariable("data", templateBaseMapper);

        // Generate HTML from template
        String htmlTemplate = templateEngine.process(appProperties.getTemplates().getPdfTemplatesClasspath() + templateName, context);

        // Prepare the output stream for the PDF
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        // Create PdfWriter and PdfDocument instances
        PdfWriter writer = new PdfWriter(target);
        PdfDocument pdfDocument = new PdfDocument(writer);

        // Set the landscape page size
        Document document = new Document(pdfDocument, PageSize.A4.rotate());

        // Set margins for landscape as well
        document.setMargins(0, 0, 0, 0); // Adjust margins as needed

        // Set up the converter properties
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        // Convert the HTML to PDF using the landscape PdfDocument
        HtmlConverter.convertToPdf(htmlTemplate, pdfDocument, converterProperties);

        // Close the document to complete the PDF creation
        document.close();

        // Convert the generated PDF to a byte array
        byte[] pdfBytes = target.toByteArray();

        // Save the PDF to a file or return the byte array
        return savePdfToFile(pdfBytes, UUID.randomUUID().toString());
    }
}
