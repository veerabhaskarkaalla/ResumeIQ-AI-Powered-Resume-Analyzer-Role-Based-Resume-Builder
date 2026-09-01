package com.resumeiq.dto;

import java.util.List;

public class AtsAnalysisResult {

    private Long resumeId;

    private String company;
    private String role;

    private int overallScore;

    private int keywordMatch;
    private int skillsMatch;
    private int experienceRelevance;
    private int projectsRelevance;
    private int educationFit;
    private int structure;
    private int formatting;
    private int quantification;

    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> missingKeywords;

    private String recommendation;

    public AtsAnalysisResult() {
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(
            Long resumeId) {

        this.resumeId = resumeId;
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

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(
            int overallScore) {

        this.overallScore = overallScore;
    }

    public int getKeywordMatch() {
        return keywordMatch;
    }

    public void setKeywordMatch(
            int keywordMatch) {

        this.keywordMatch = keywordMatch;
    }

    public int getSkillsMatch() {
        return skillsMatch;
    }

    public void setSkillsMatch(
            int skillsMatch) {

        this.skillsMatch = skillsMatch;
    }

    public int getExperienceRelevance() {
        return experienceRelevance;
    }

    public void setExperienceRelevance(
            int experienceRelevance) {

        this.experienceRelevance =
                experienceRelevance;
    }

    public int getProjectsRelevance() {
        return projectsRelevance;
    }

    public void setProjectsRelevance(
            int projectsRelevance) {

        this.projectsRelevance =
                projectsRelevance;
    }

    public int getEducationFit() {
        return educationFit;
    }

    public void setEducationFit(
            int educationFit) {

        this.educationFit = educationFit;
    }

    public int getStructure() {
        return structure;
    }

    public void setStructure(
            int structure) {

        this.structure = structure;
    }

    public int getFormatting() {
        return formatting;
    }

    public void setFormatting(
            int formatting) {

        this.formatting = formatting;
    }

    public int getQuantification() {
        return quantification;
    }

    public void setQuantification(
            int quantification) {

        this.quantification =
                quantification;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(
            List<String> matchedSkills) {

        this.matchedSkills =
                matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(
            List<String> missingSkills) {

        this.missingSkills =
                missingSkills;
    }

    public List<String> getMissingKeywords() {
        return missingKeywords;
    }

    public void setMissingKeywords(
            List<String> missingKeywords) {

        this.missingKeywords =
                missingKeywords;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(
            String recommendation) {

        this.recommendation =
                recommendation;
    }
}