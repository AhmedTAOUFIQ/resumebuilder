package com.keiken.pdfTemplateGenerator.Mapper;

import com.keiken.mapper.TemplateBaseMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("keiken.html")
public class KeikenTemplateMapper extends TemplateBaseMapper {

    public String[] getSkills() {
       return facts.stream()
                .flatMap(fact -> fact.getLinkedSkills().stream())
                .map(Skill::getSkillName)
                .distinct()
                .map(skill -> {
                    String formattedString = skill.replace("_", " ").toLowerCase();
                    formattedString = formattedString.substring(0, 1).toUpperCase() + formattedString.substring(1);
                    return formattedString;
                })
                .toList().toArray(new String[0]);
    }

    public String[] getCertifications() {
        return facts.stream()
                .filter(fact -> fact.getFactType() == FactType.certification)
                .map(TemplateBaseMapper.Fact::getFactTitle)
                .map(certif -> {
                    String fromatedString = certif.replace("_", " ").toLowerCase();
                    fromatedString = fromatedString.substring(0, 1).toUpperCase() + fromatedString.substring(1);
                    return fromatedString;
                })
                .toArray(String[]::new);
    }
}
