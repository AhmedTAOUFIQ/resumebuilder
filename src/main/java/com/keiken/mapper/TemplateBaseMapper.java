package com.keiken.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TemplateBaseMapper {

    protected String name;
    protected String city;
    protected String abstractProfile;
    protected String cvRole;
    protected String roleDescription;
    protected List<Fact> facts;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Skill {
        private String id;
        private String skillName;
        private String level;

        @Override
        public String toString() {
            return skillName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Skill skill = (Skill) o;
            return skillName.equals(skill.skillName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(skillName);
        }
    }

    public enum FactType {
        experience, certification, education
    }

    @JsonIgnore
    public List<PlaceholderImageMapping> getPlaceholderImageMappings() {
        return new ArrayList<>();
    }

    public void setProps() {
    }

}
