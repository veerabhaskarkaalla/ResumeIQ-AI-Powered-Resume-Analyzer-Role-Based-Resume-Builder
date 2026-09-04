package com.resumeiq.dto;

import java.util.ArrayList;
import java.util.List;

public class SkillCategory {

    private String name;

    private List<String> skills =
            new ArrayList<>();


    public SkillCategory() {
    }


    public SkillCategory(
            String name,
            List<String> skills) {

        this.name = name;
        this.skills = skills;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<String> getSkills() {
        return skills;
    }


    public void setSkills(
            List<String> skills) {

        this.skills = skills;
    }
}