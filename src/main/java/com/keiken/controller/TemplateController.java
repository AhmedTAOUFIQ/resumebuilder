package com.keiken.controller;

import com.keiken.dto.TemplateRequest;
import com.keiken.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("pptemplate")
    public ResponseEntity<byte[]> createPPTemplate(@RequestBody TemplateRequest templateRequest) {
        byte[] pptxData = templateService.processTemplate(
                templateRequest.templateName() + ".pptx",
                templateRequest.data()
        );

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + templateRequest.templateName() + ".pptx")
                .body(pptxData);
    }

    @PostMapping("pdftemplate")
    public ResponseEntity<byte[]> createPdfTemplate(@RequestBody TemplateRequest templateRequest) {
        byte[] templateContent = templateService.processTemplate(
                templateRequest.templateName() + ".html",
                templateRequest.data()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", templateRequest.templateName() + ".pdf");

        return new ResponseEntity<>(templateContent, headers, HttpStatus.OK);
    }



}
