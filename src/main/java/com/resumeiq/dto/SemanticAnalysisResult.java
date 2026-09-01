package com.resumeiq.dto;

import java.util.List;

public class SemanticAnalysisResult {

    private int semanticMatchScore;

    private String summary;

    private List<String> strengths;

    private List<String> gaps;

    private List<String> relevantExperience;

    private List<String> relevantProjects;

    private List<String> suggestions;

    public SemanticAnalysisResult() {
    }

    public int getSemanticMatchScore() {
        return semanticMatchScore;
    }

    public void setSemanticMatchScore(
            int semanticMatchScore) {

        this.semanticMatchScore =
                semanticMatchScore;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(
            String summary) {

        this.summary = summary;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(
            List<String> strengths) {

        this.strengths = strengths;
    }

    public List<String> getGaps() {
        return gaps;
    }

    public void setGaps(
            List<String> gaps) {

        this.gaps = gaps;
    }

    public List<String> getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(
            List<String> relevantExperience) {

        this.relevantExperience =
                relevantExperience;
    }

    public List<String> getRelevantProjects() {
        return relevantProjects;
    }

    public void setRelevantProjects(
            List<String> relevantProjects) {

        this.relevantProjects =
                relevantProjects;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(
            List<String> suggestions) {

        this.suggestions = suggestions;
    }
}