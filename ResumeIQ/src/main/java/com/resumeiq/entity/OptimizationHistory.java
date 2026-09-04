package com.resumeiq.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "optimization_history")
public class OptimizationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long resumeId;

    private Long optimizedVersionId;

    private String company;

    private String role;

    private Integer beforeScore;

    private Integer afterScore;

    private Integer improvement;

    private LocalDateTime createdAt;

    public OptimizationHistory() {
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

    public Long getOptimizedVersionId() {
        return optimizedVersionId;
    }

    public void setOptimizedVersionId(Long optimizedVersionId) {
        this.optimizedVersionId = optimizedVersionId;
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

    public Integer getBeforeScore() {
        return beforeScore;
    }

    public void setBeforeScore(Integer beforeScore) {
        this.beforeScore = beforeScore;
    }

    public Integer getAfterScore() {
        return afterScore;
    }

    public void setAfterScore(Integer afterScore) {
        this.afterScore = afterScore;
    }

    public Integer getImprovement() {
        return improvement;
    }

    public void setImprovement(Integer improvement) {
        this.improvement = improvement;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}