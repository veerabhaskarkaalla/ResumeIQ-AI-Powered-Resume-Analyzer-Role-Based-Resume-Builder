package com.resumeiq.dto;

import java.util.List;

public class OptimizationComparisonResult {

    private Long resumeId;

    private Long optimizedVersionId;

    private Integer versionNumber;

    private String company;

    private String role;

    // =========================================================
    // OVERALL
    // =========================================================

    private int beforeScore;

    private int afterScore;

    private int improvement;


    // =========================================================
    // KEYWORDS
    // =========================================================

    private int beforeKeywordMatch;

    private int afterKeywordMatch;


    // =========================================================
    // SKILLS
    // =========================================================

    private int beforeSkillsMatch;

    private int afterSkillsMatch;


    // =========================================================
    // EXPERIENCE
    // =========================================================

    private int beforeExperienceRelevance;

    private int afterExperienceRelevance;


    // =========================================================
    // PROJECTS
    // =========================================================

    private int beforeProjectsRelevance;

    private int afterProjectsRelevance;


    // =========================================================
    // EDUCATION
    // =========================================================

    private int beforeEducationFit;

    private int afterEducationFit;


    // =========================================================
    // STRUCTURE
    // =========================================================

    private int beforeStructure;

    private int afterStructure;


    // =========================================================
    // FORMATTING
    // =========================================================

    private int beforeFormatting;

    private int afterFormatting;


    // =========================================================
    // QUANTIFICATION
    // =========================================================

    private int beforeQuantification;

    private int afterQuantification;


    // =========================================================
    // OPTIMIZATION OUTPUT
    // =========================================================

    private String optimizedResumeText;

    private List<String> skillsToHighlight;

    private List<String> missingSkillsNotAdded;

    private List<String> rejectedExperienceBullets;

    private List<String> rejectedProjectBullets;

    private String status;


    public OptimizationComparisonResult() {
    }


    public Long getResumeId() {
        return resumeId;
    }


    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }


    public Long getOptimizedVersionId() {
        return optimizedVersionId;
    }


    public void setOptimizedVersionId(
            Long optimizedVersionId) {

        this.optimizedVersionId =
                optimizedVersionId;
    }


    public Integer getVersionNumber() {
        return versionNumber;
    }


    public void setVersionNumber(
            Integer versionNumber) {

        this.versionNumber =
                versionNumber;
    }


    public String getCompany() {
        return company;
    }


    public void setCompany(
            String company) {

        this.company = company;
    }


    public String getRole() {
        return role;
    }


    public void setRole(
            String role) {

        this.role = role;
    }


    // =========================================================
    // OVERALL
    // =========================================================

    public int getBeforeScore() {
        return beforeScore;
    }


    public void setBeforeScore(
            int beforeScore) {

        this.beforeScore = beforeScore;
    }


    public int getAfterScore() {
        return afterScore;
    }


    public void setAfterScore(
            int afterScore) {

        this.afterScore = afterScore;
    }


    public int getImprovement() {
        return improvement;
    }


    public void setImprovement(
            int improvement) {

        this.improvement = improvement;
    }


    // =========================================================
    // KEYWORDS
    // =========================================================

    public int getBeforeKeywordMatch() {
        return beforeKeywordMatch;
    }


    public void setBeforeKeywordMatch(
            int beforeKeywordMatch) {

        this.beforeKeywordMatch =
                beforeKeywordMatch;
    }


    public int getAfterKeywordMatch() {
        return afterKeywordMatch;
    }


    public void setAfterKeywordMatch(
            int afterKeywordMatch) {

        this.afterKeywordMatch =
                afterKeywordMatch;
    }


    // =========================================================
    // SKILLS
    // =========================================================

    public int getBeforeSkillsMatch() {
        return beforeSkillsMatch;
    }


    public void setBeforeSkillsMatch(
            int beforeSkillsMatch) {

        this.beforeSkillsMatch =
                beforeSkillsMatch;
    }


    public int getAfterSkillsMatch() {
        return afterSkillsMatch;
    }


    public void setAfterSkillsMatch(
            int afterSkillsMatch) {

        this.afterSkillsMatch =
                afterSkillsMatch;
    }


    // =========================================================
    // EXPERIENCE
    // =========================================================

    public int getBeforeExperienceRelevance() {
        return beforeExperienceRelevance;
    }


    public void setBeforeExperienceRelevance(
            int beforeExperienceRelevance) {

        this.beforeExperienceRelevance =
                beforeExperienceRelevance;
    }


    public int getAfterExperienceRelevance() {
        return afterExperienceRelevance;
    }


    public void setAfterExperienceRelevance(
            int afterExperienceRelevance) {

        this.afterExperienceRelevance =
                afterExperienceRelevance;
    }


    // =========================================================
    // PROJECTS
    // =========================================================

    public int getBeforeProjectsRelevance() {
        return beforeProjectsRelevance;
    }


    public void setBeforeProjectsRelevance(
            int beforeProjectsRelevance) {

        this.beforeProjectsRelevance =
                beforeProjectsRelevance;
    }


    public int getAfterProjectsRelevance() {
        return afterProjectsRelevance;
    }


    public void setAfterProjectsRelevance(
            int afterProjectsRelevance) {

        this.afterProjectsRelevance =
                afterProjectsRelevance;
    }


    // =========================================================
    // EDUCATION
    // =========================================================

    public int getBeforeEducationFit() {
        return beforeEducationFit;
    }


    public void setBeforeEducationFit(
            int beforeEducationFit) {

        this.beforeEducationFit =
                beforeEducationFit;
    }


    public int getAfterEducationFit() {
        return afterEducationFit;
    }


    public void setAfterEducationFit(
            int afterEducationFit) {

        this.afterEducationFit =
                afterEducationFit;
    }


    // =========================================================
    // STRUCTURE
    // =========================================================

    public int getBeforeStructure() {
        return beforeStructure;
    }


    public void setBeforeStructure(
            int beforeStructure) {

        this.beforeStructure =
                beforeStructure;
    }


    public int getAfterStructure() {
        return afterStructure;
    }


    public void setAfterStructure(
            int afterStructure) {

        this.afterStructure =
                afterStructure;
    }


    // =========================================================
    // FORMATTING
    // =========================================================

    public int getBeforeFormatting() {
        return beforeFormatting;
    }


    public void setBeforeFormatting(
            int beforeFormatting) {

        this.beforeFormatting =
                beforeFormatting;
    }


    public int getAfterFormatting() {
        return afterFormatting;
    }


    public void setAfterFormatting(
            int afterFormatting) {

        this.afterFormatting =
                afterFormatting;
    }


    // =========================================================
    // QUANTIFICATION
    // =========================================================

    public int getBeforeQuantification() {
        return beforeQuantification;
    }


    public void setBeforeQuantification(
            int beforeQuantification) {

        this.beforeQuantification =
                beforeQuantification;
    }


    public int getAfterQuantification() {
        return afterQuantification;
    }


    public void setAfterQuantification(
            int afterQuantification) {

        this.afterQuantification =
                afterQuantification;
    }


    // =========================================================
    // OUTPUT
    // =========================================================

    public String getOptimizedResumeText() {
        return optimizedResumeText;
    }


    public void setOptimizedResumeText(
            String optimizedResumeText) {

        this.optimizedResumeText =
                optimizedResumeText;
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


    public List<String> getRejectedExperienceBullets() {
        return rejectedExperienceBullets;
    }


    public void setRejectedExperienceBullets(
            List<String> rejectedExperienceBullets) {

        this.rejectedExperienceBullets =
                rejectedExperienceBullets;
    }


    public List<String> getRejectedProjectBullets() {
        return rejectedProjectBullets;
    }


    public void setRejectedProjectBullets(
            List<String> rejectedProjectBullets) {

        this.rejectedProjectBullets =
                rejectedProjectBullets;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(
            String status) {

        this.status = status;
    }
}