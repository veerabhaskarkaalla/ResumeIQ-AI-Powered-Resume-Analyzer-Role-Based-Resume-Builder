package com.resumeiq.dto;

import java.util.List;

public class AiOptimizationCandidate {

    private String professionalSummary;

    private List<String> experienceBullets;

    private List<String> projectBullets;

    private List<String> skillsToHighlight;

    private List<String> missingSkillsNotAdded;

    private List<String> changes;

    public AiOptimizationCandidate() {
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public List<String> getExperienceBullets() {
        return experienceBullets;
    }

    public void setExperienceBullets(List<String> experienceBullets) {
        this.experienceBullets = experienceBullets;
    }

    public List<String> getProjectBullets() {
        return projectBullets;
    }

    public void setProjectBullets(List<String> projectBullets) {
        this.projectBullets = projectBullets;
    }

    public List<String> getSkillsToHighlight() {
        return skillsToHighlight;
    }

    public void setSkillsToHighlight(List<String> skillsToHighlight) {
        this.skillsToHighlight = skillsToHighlight;
    }

    public List<String> getMissingSkillsNotAdded() {
        return missingSkillsNotAdded;
    }

    public void setMissingSkillsNotAdded(List<String> missingSkillsNotAdded) {
        this.missingSkillsNotAdded = missingSkillsNotAdded;
    }

    public List<String> getChanges() {
        return changes;
    }

    public void setChanges(List<String> changes) {
        this.changes = changes;
    }
}