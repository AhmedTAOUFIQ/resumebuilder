package com.keiken.controller;

import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.openai.service.SummaryService;
import com.keiken.pdfTemplateGenerator.Mapper.KeikenTemplateMapperPPT;
import com.keiken.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PdfViewController {

    private final TemplateService templateService;
    private final SummaryService summaryService;

    @GetMapping("/view")
    public String viewResume(Model model) {
        try {
            TemplateBaseMapper data = templateService.loadResumeFromJson();
            model.addAttribute("data", data);

            String abstractProfile=data.getAbstractProfile();
            String summarizedAbstractProfile=summaryService.getSummary(abstractProfile,20);
            data.setAbstractProfile(summarizedAbstractProfile);

            List<TemplateBaseMapper.Fact> facts = data.getFacts();
            KeikenTemplateMapperPPT builder = new KeikenTemplateMapperPPT();
            List<List<TemplateBaseMapper.Fact>> experienceRows = builder.buildExperienceRows(facts);

            model.addAttribute("experienceRows", experienceRows);

            templateService.processTemplate("pdfHtmlTemplate.html", data);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Could not load the resume data.");
        }

        return "pdfHtmlTemplate";
    }
}
