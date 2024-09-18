package com.keiken.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Builder @Getter @Setter
public class TemplateBaseMapper {

    protected String name;
    protected String city;
    protected String abstractProfile;
    protected String cvRole;
    protected List<Fact> facts;

    @Getter @Setter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class Fact {
        private String factTitle;
        private String startDate;
        private String endDate;
        public FactType factType;
        private String factDescription;
        private String location;
        private String city;
        private List<Skill> linkedSkills;
    }

    @Getter @Setter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class Skill {
        private String id;
        private String skillName;
        @Override
        public String toString() {
            return skillName;
        }
    }

   public enum FactType {
        experience, certification , education
    }

    // To override by implementing class
    @JsonIgnore
    public List<PlaceholderImageMapping> getPlaceholderImageMappings() {
        return new ArrayList<>();
    }

    // To override by implementing class
    public void setProps() {}



}
