package com.resumeiq.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "resume_versions")
public class ResumeVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long resumeId;

    private Integer versionNumber;

    private String versionType;

    private String company;

    private String role;

    private Integer atsScore;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    /*
     * Production structured snapshot.
     *
     * PDF/DOCX renderer uses this JSON instead
     * of reparsing formatted resume text.
     */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String structuredContent;

    private LocalDateTime createdAt;


    public ResumeVersion() {
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


    public Integer getVersionNumber() {
        return versionNumber;
    }


    public void setVersionNumber(
            Integer versionNumber) {

        this.versionNumber =
                versionNumber;
    }


    public String getVersionType() {
        return versionType;
    }


    public void setVersionType(
            String versionType) {

        this.versionType =
                versionType;
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


    public Integer getAtsScore() {
        return atsScore;
    }


    public void setAtsScore(
            Integer atsScore) {

        this.atsScore = atsScore;
    }


    public String getContent() {
        return content;
    }


    public void setContent(
            String content) {

        this.content = content;
    }


    public String getStructuredContent() {
        return structuredContent;
    }


    public void setStructuredContent(
            String structuredContent) {

        this.structuredContent =
                structuredContent;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }
}