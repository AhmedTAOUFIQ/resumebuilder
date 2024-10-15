package com.keiken.controller;

import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.pdfTemplateGenerator.Mapper.KeikenTemplateMapper;
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
public class ViewController {

    private final TemplateService templateService;

    @GetMapping("/view")
    public String viewResume(Model model) {
        try {
            TemplateBaseMapper data = templateService.loadResumeFromJson();
            model.addAttribute("data", data);
            List<TemplateBaseMapper.Fact> facts = data.getFacts(); // Assuming this is how you get the facts
            KeikenTemplateMapper builder = new KeikenTemplateMapper();
            List<List<TemplateBaseMapper.Fact>> experienceRows = builder.buildExperienceRows(facts);

// Add the grouped rows to your model
            model.addAttribute("experienceRows", experienceRows);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Could not load the resume data.");
        }

        return "pdfHtmlTemplate";
    }
}
