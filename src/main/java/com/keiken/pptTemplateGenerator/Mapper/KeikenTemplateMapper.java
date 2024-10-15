package com.keiken.pptTemplateGenerator.Mapper;

import com.keiken.mapper.PlaceholderImageMapping;
import com.keiken.pptTemplateGenerator.shapes.ProgressCircle;
import com.keiken.mapper.TemplateBaseMapper;
import lombok.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Component("keiken.pptx")
public class KeikenTemplateMapper extends TemplateBaseMapper {
    private String firstEducationYear;
    private String firstEducationDescription;
    private String secondEducationYear;
    private String secondEducationDescription;
    private String projectType;
    private String x;
    private String o;
    private String z;
    private String certifications;

    @Override
    public void setProps() {
        List<Fact> educations = getEducation();

        this.firstEducationYear = educations.get(0).getEndDate();
        this.firstEducationDescription = educations.get(0).getFactDescription();
        this.secondEducationYear = educations.get(1).getEndDate();
        this.secondEducationDescription = educations.get(1).getFactDescription();
        this.x = getExperienceYears();
        this.o = String.valueOf(getEducation().size());
        this.z = String.valueOf(getCertifications().length);
        this.projectType = "Data analysis project";
        this.certifications = String.join(", ", getCertifications());
    }

    @Override
    public List<PlaceholderImageMapping> getPlaceholderImageMappings() {
        return List.of(
                new PlaceholderImageMapping() {
                    public String getPlaceholdersPattern() {return "TECH_%d";}
                    public String getDataSourceMethod() {return "getSkills";}
                    public String getImageFolderPath() {return "technologies/";}
                    public String getFallbackImageName() {return "elk";}
                    public int getNumberOfImagesToReplace() {return 10;}
                    public ProgressCircle applyProgressAroundImage() {
                        return ProgressCircle.builder()
                                .color(new Color(77, 112, 140))
                                .radius(26.5)
                                .borderThickness(1.7)
                                .build();
                    }
                    public String getProgressPercentagesMethod() { return "getSkillsPercentages";}
                },
                new PlaceholderImageMapping() {
                    public String getPlaceholdersPattern() {return "CERTIF_%d";}
                    public String getDataSourceMethod() {return "getCertificationsImages";}
                    public String getImageFolderPath() {return "certifications/";}
                    public String getFallbackImageName() {return "ccna";}
                    public int getNumberOfImagesToReplace() {return 5;}
                    public ProgressCircle applyProgressAroundImage() {return null;}
                    public String getProgressPercentagesMethod(){return "";}
                },
                new PlaceholderImageMapping() {
                    public String getPlaceholdersPattern() {return "EDUCATION_%d";}
                    public String getDataSourceMethod() {return "getSchools";}
                    public String getImageFolderPath() {return "schools/";}
                    public String getFallbackImageName() {return "fsbenmsik";}
                    public int getNumberOfImagesToReplace() {return 2;}
                    public ProgressCircle applyProgressAroundImage() {return null;}
                    public String getProgressPercentagesMethod(){return "";}
                }
        );
    }

    /*public String[] getSkills() {
        return facts.stream()
                .flatMap(fact -> fact.getLinkedSkills().stream())
                .map(Skill::getSkillName)
                .distinct()
                .toArray(String[]::new);
    }*/

    public Integer[] getSkillsPercentages() {
        return new Integer[]{30, 50, 70, 80, 40, 100, 45, 50, 60, 95};
    }

    public String[] getCertifications() {
        return facts.stream()
                .filter(fact -> fact.getFactType() == FactType.certification)
                .map(Fact::getFactTitle)
                .toArray(String[]::new);
    }

    public String[] getCertificationsImages() {
        return facts.stream()
                .filter(fact -> fact.getFactType() == FactType.certification)
                .map(fact -> {
                    String[] factWords =  fact.getFactTitle().toLowerCase().split(" ");
                    return  String.join("_", factWords);
                })
                .toArray(String[]::new);
    }

    public String[] getSchools() {
        return getEducation().stream()
                .map(Fact::getFactTitle)
                .toArray(String[]::new);
    }

    public List<Fact> getEducation() {
        return facts.stream()
                .filter(fact -> fact.getFactType() == FactType.education)
                .collect(Collectors.toList());
    }

    public String getExperienceYears() {
        int max= Integer.MIN_VALUE;
        int min= Integer.MAX_VALUE;

        for (Fact fact : facts) {
            if(fact.getStartDate() != null) {
                int startDate = Integer.parseInt(fact.getStartDate());
                min = Math.min(startDate, min);
            }

            if(fact.getEndDate() != null) {
                int endDate = Integer.parseInt(fact.getEndDate());
                max = Math.max(endDate, max);
            }
        }

        return "+" + (max - min);
    }

}
