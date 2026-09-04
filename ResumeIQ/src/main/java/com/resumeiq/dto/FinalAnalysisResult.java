package com.resumeiq.dto;

import java.util.List;

public class FinalAnalysisResult {

    private Long resumeId;

    private String company;

    private String role;

    private int atsScore;

    private int semanticScore;

    private int finalScore;

    private int keywordMatch;

    private int skillsMatch;

    private int experienceRelevance;

    private int projectsRelevance;

    private int structure;

    private int formatting;

    private int quantification;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    private List<String> verifiedStrengths;

    private List<String> rejectedStrengths;

    private List<String> gaps;

    private List<String> suggestions;

    private String aiSummary;

    private String recommendation;


    public FinalAnalysisResult() {
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


    public int getAtsScore() {
        return atsScore;
    }


    public void setAtsScore(int atsScore) {
        this.atsScore = atsScore;
    }


    public int getSemanticScore() {
        return semanticScore;
    }


    public void setSemanticScore(
            int semanticScore) {

        this.semanticScore = semanticScore;
    }


    public int getFinalScore() {
        return finalScore;
    }


    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
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


    public int getStructure() {
        return structure;
    }


    public void setStructure(int structure) {
        this.structure = structure;
    }


    public int getFormatting() {
        return formatting;
    }


    public void setFormatting(int formatting) {
        this.formatting = formatting;
    }


    public int getQuantification() {
        return quantification;
    }


    public void setQuantification(
            int quantification) {

        this.quantification = quantification;
    }


    public List<String> getMatchedSkills() {
        return matchedSkills;
    }


    public void setMatchedSkills(
            List<String> matchedSkills) {

        this.matchedSkills = matchedSkills;
    }


    public List<String> getMissingSkills() {
        return missingSkills;
    }


    public void setMissingSkills(
            List<String> missingSkills) {

        this.missingSkills = missingSkills;
    }


    public List<String> getVerifiedStrengths() {
        return verifiedStrengths;
    }


    public void setVerifiedStrengths(
            List<String> verifiedStrengths) {

        this.verifiedStrengths =
                verifiedStrengths;
    }


    public List<String> getRejectedStrengths() {
        return rejectedStrengths;
    }


    public void setRejectedStrengths(
            List<String> rejectedStrengths) {

        this.rejectedStrengths =
                rejectedStrengths;
    }


    public List<String> getGaps() {
        return gaps;
    }


    public void setGaps(List<String> gaps) {
        this.gaps = gaps;
    }


    public List<String> getSuggestions() {
        return suggestions;
    }


    public void setSuggestions(
            List<String> suggestions) {

        this.suggestions = suggestions;
    }


    public String getAiSummary() {
        return aiSummary;
    }


    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
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