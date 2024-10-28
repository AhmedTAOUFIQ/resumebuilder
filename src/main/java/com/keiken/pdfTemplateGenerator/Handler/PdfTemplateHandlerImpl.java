package com.keiken.pdfTemplateGenerator.Handler;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
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
    public static final String ARIAL = "src/main/resources/static/fonts/arial";

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
    public byte[] generatePdf(String templateName, TemplateBaseMapper templateBaseMapper, boolean isLandscape) {
        Context context = new Context();
        context.setVariable("data", templateBaseMapper);

        // 1. Generate the HTML template content
        String htmlTemplate = templateEngine.process(appProperties.getTemplates().getPdfTemplatesClasspath() + templateName, context);

        // 2. Prepare a ByteArrayOutputStream to hold the PDF data
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        // 3. Prepare PdfWriter and PdfDocument
        PdfWriter writer = new PdfWriter(target);
        PdfDocument pdfDocument = new PdfDocument(writer);

        // 4. Set the page size (portrait or landscape) based on the isLandscape parameter
        PageSize pageSize = isLandscape ? PageSize.A4.rotate() : PageSize.A4;
        Document document = new Document(pdfDocument, pageSize);

        // Set margins (customize them as needed)
        document.setMargins(0, 0, 0, 0);

        // 5. Prepare ConverterProperties with FontProvider for Arial font
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addDirectory(ARIAL); // Assuming ARIAL is a constant directory path to the Arial font
        converterProperties.setFontProvider(fontProvider);

        // Set base URI (adjust if needed)
        converterProperties.setBaseUri("http://localhost:8080");

        // 6. Convert the HTML content into the PDF document
        HtmlConverter.convertToPdf(htmlTemplate, pdfDocument, converterProperties);

        // Close the document to complete the PDF generation
        document.close();

        // 7. Return the generated PDF as byte array
        byte[] pdfBytes = target.toByteArray();
        return savePdfToFile(pdfBytes, UUID.randomUUID().toString());
    }

}
