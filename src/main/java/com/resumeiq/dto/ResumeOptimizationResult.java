package com.resumeiq.dto;

import java.util.List;

public class ResumeOptimizationResult {

    private Long resumeId;

    private String company;

    private String role;

    private String optimizedSummary;

    private List<String> verifiedExperienceBullets;

    private List<String> rejectedExperienceBullets;

    private List<String> verifiedProjectBullets;

    private List<String> rejectedProjectBullets;

    private List<String> skillsToHighlight;

    private List<String> missingSkillsNotAdded;

    private List<String> changes;

    private String status;

    public ResumeOptimizationResult() {
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
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

    public String getOptimizedSummary() {
        return optimizedSummary;
    }

    public void setOptimizedSummary(String optimizedSummary) {
        this.optimizedSummary = optimizedSummary;
    }

    public List<String> getVerifiedExperienceBullets() {
        return verifiedExperienceBullets;
    }

    public void setVerifiedExperienceBullets(
            List<String> verifiedExperienceBullets) {

        this.verifiedExperienceBullets =
                verifiedExperienceBullets;
    }

    public List<String> getRejectedExperienceBullets() {
        return rejectedExperienceBullets;
    }

    public void setRejectedExperienceBullets(
            List<String> rejectedExperienceBullets) {

        this.rejectedExperienceBullets =
                rejectedExperienceBullets;
    }

    public List<String> getVerifiedProjectBullets() {
        return verifiedProjectBullets;
    }

    public void setVerifiedProjectBullets(
            List<String> verifiedProjectBullets) {

        this.verifiedProjectBullets =
                verifiedProjectBullets;
    }

    public List<String> getRejectedProjectBullets() {
        return rejectedProjectBullets;
    }

    public void setRejectedProjectBullets(
            List<String> rejectedProjectBullets) {

        this.rejectedProjectBullets =
                rejectedProjectBullets;
    }

    public List<String> getSkillsToHighlight() {
        return skillsToHighlight;
    }

    public void setSkillsToHighlight(
            List<String> skillsToHighlight) {

        this.skillsToHighlight =
                skillsToHighlight;
    }

    public List<String> getMissingSkillsNotAdded() {
        return missingSkillsNotAdded;
    }

    public void setMissingSkillsNotAdded(
            List<String> missingSkillsNotAdded) {

        this.missingSkillsNotAdded =
                missingSkillsNotAdded;
    }

    public List<String> getChanges() {
        return changes;
    }

    public void setChanges(List<String> changes) {
        this.changes = changes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}