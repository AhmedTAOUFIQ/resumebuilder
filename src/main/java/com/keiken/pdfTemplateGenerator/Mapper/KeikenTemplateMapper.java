package com.keiken.pdfTemplateGenerator.Mapper;

import com.keiken.mapper.TemplateBaseMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component("pdfHtmlTemplate.html")
public class KeikenTemplateMapper extends TemplateBaseMapper {
    protected String yearsExperience;
    protected String projectCount;
    protected String certificationCount;
    protected String[] skills;
    protected String[] certifications;
    protected String[] certificationsIcons;
    protected String[] experiences;

    public List<List<Fact>> buildExperienceRows(List<Fact> facts) {
        List<List<Fact>> rows = new ArrayList<>();
        List<Fact> currentRow = new ArrayList<>();

        for (Fact fact : facts) {
            if ("experience".equals(fact.getFactType().name())) {
                currentRow.add(fact);
                // If the row has 3 items, add it to rows and start a new one
                if (currentRow.size() == 3) {
                    rows.add(currentRow);
                    currentRow = new ArrayList<>();
                }
            }
        }
        // Add any remaining items in the last row
        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        return rows;
    }
    public String getYearsExperience() {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (Fact fact : facts) {
            if (fact.getStartDate() != null) {
                int startDate = Integer.parseInt(fact.getStartDate());
                min = Math.min(startDate, min);
            }

            if (fact.getEndDate() != null) {
                int endDate = Integer.parseInt(fact.getEndDate());
                max = Math.max(endDate, max);
            }
        }

        return "+" + (max - min);
    }

    public String getProjectCount() {
        return "+" + 10;
    }

    public String getCertificationCount() {
        return "" + getCertifications().length;
    }

    public List<Skill> getSkills() {
        return facts.stream()
                .flatMap(fact -> fact.getLinkedSkills().stream())
                .collect(Collectors.toCollection(() -> new LinkedHashSet<>()))
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }


    public String[] getCertifications() {
        return facts.stream()
                .filter(fact -> fact.getFactType() == FactType.certification)
                .map(TemplateBaseMapper.Fact::getFactTitle)
                .map(certif -> {
                    String fromatedString = certif.replace("_", " ").toLowerCase();
                    return fromatedString;
                })
                .toArray(String[]::new);
    }

    public String[] getCertificationsIcons() {
        return facts.stream()
                .filter(fact -> fact.getFactType() == FactType.certification)
                .map(TemplateBaseMapper.Fact::getFactTitle)
                .map(certif -> {
                    String fromatedString = certif.replace(" ", "_").toLowerCase();
                    return fromatedString;
                })
                .toArray(String[]::new);
    }

    public String[] getExperiences() {
        return facts.stream()
                .filter(fact -> fact.getFactType() == FactType.experience)
                .map(TemplateBaseMapper.Fact::getFactTitle)
                .toArray(String[]::new);
    }

}
