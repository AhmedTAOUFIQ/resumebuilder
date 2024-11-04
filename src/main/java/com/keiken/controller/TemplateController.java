package com.keiken.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keiken.dto.TemplateRequest;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.openai.service.SummaryService;
import com.keiken.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;
    private final SummaryService summaryService;

    @PostMapping("pptTemplate")
    public ResponseEntity<byte[]> createPPTemplate(boolean isLandscape,@RequestBody TemplateRequest templateRequest) throws IOException {
        byte[] pptxData = templateService.processTemplate(
                templateRequest.templateName() + ".pptx",
                templateRequest.data(),
                isLandscape
        );

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + templateRequest.templateName() + ".pptx")
                .body(pptxData);
    }

    @PostMapping("pdfTemplate")
    public ResponseEntity<byte[]> createPdfTemplate(boolean isLandscape,@RequestBody TemplateRequest templateRequest) throws IOException {

        String abstractProfile = templateRequest.data().getAbstractProfile();
        String summarizedAbstractProfile = summaryService.getSummary(abstractProfile, 20);
        templateRequest.data().setAbstractProfile(summarizedAbstractProfile);

        byte[] templateContent = templateService.processTemplate(
                templateRequest.templateName() + ".html",
                templateRequest.data(),
                isLandscape
        );


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", templateRequest.templateName() + ".pdf");

        return new ResponseEntity<>(templateContent, headers, HttpStatus.OK);
    }

}
