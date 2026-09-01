package com.resumeiq.dto;

import java.time.LocalDateTime;

public class OptimizationHistorySummary {

    private Long id;

    private Long optimizedVersionId;

    private String company;

    private String role;

    private Integer beforeScore;

    private Integer afterScore;

    private Integer improvement;

    private LocalDateTime createdAt;


    public OptimizationHistorySummary() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getOptimizedVersionId() {
        return optimizedVersionId;
    }


    public void setOptimizedVersionId(
            Long optimizedVersionId) {

        this.optimizedVersionId =
                optimizedVersionId;
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