package com.resumeiq.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "ats_analysis_history")
public class AtsAnalysisHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long resumeId;

    private String company;

    private String role;

    private Integer atsScore;

    private Integer semanticScore;

    private Integer finalScore;

    private Integer keywordMatch;

    private Integer skillsMatch;

    private LocalDateTime createdAt;


    public AtsAnalysisHistory() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public Integer getAtsScore() {
        return atsScore;
    }


    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
    }


    public Integer getSemanticScore() {
        return semanticScore;
    }


    public void setSemanticScore(Integer semanticScore) {
        this.semanticScore = semanticScore;
    }


    public Integer getFinalScore() {
        return finalScore;
    }


    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }


    public Integer getKeywordMatch() {
        return keywordMatch;
    }


    public void setKeywordMatch(Integer keywordMatch) {
        this.keywordMatch = keywordMatch;
    }


    public Integer getSkillsMatch() {
        return skillsMatch;
    }


    public void setSkillsMatch(Integer skillsMatch) {
        this.skillsMatch = skillsMatch;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}