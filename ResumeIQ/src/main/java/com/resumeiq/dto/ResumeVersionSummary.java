package com.resumeiq.dto;

import java.time.LocalDateTime;

public class ResumeVersionSummary {

    private Long id;

    private Integer versionNumber;

    private String versionType;

    private String company;

    private String role;

    private Integer atsScore;

    private LocalDateTime createdAt;


    public ResumeVersionSummary() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Integer getVersionNumber() {
        return versionNumber;
    }


    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }


    public String getVersionType() {
        return versionType;
    }


    public void setVersionType(String versionType) {
        this.versionType = versionType;
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


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}