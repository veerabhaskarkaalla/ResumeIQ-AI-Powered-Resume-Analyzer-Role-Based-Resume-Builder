package com.resumeiq.dto;

import java.util.List;

public class JobDescriptionAnalysis {

    private String company;
    private String role;

    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private List<String> keywords;

    private int minimumExperienceYears;

    public JobDescriptionAnalysis() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(
            List<String> requiredSkills) {

        this.requiredSkills =
                requiredSkills;
    }

    public List<String> getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(
            List<String> preferredSkills) {

        this.preferredSkills =
                preferredSkills;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(
            List<String> keywords) {

        this.keywords = keywords;
    }

    public int getMinimumExperienceYears() {
        return minimumExperienceYears;
    }

    public void setMinimumExperienceYears(
            int minimumExperienceYears) {

        this.minimumExperienceYears =
                minimumExperienceYears;
    }
}