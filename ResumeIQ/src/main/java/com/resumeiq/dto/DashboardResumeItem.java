package com.resumeiq.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardResumeItem {

    private Long resumeId;

    private String fileName;

    private String fileType;

    private LocalDateTime uploadedAt;

    private Integer latestScore;

    private List<ResumeVersionSummary> versions;

    private List<AtsHistorySummary> analyses;

    private List<OptimizationHistorySummary> optimizations;


    public DashboardResumeItem() {
    }


    public Long getResumeId() {
        return resumeId;
    }


    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileType() {
        return fileType;
    }


    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }


    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }


    public Integer getLatestScore() {
        return latestScore;
    }


    public void setLatestScore(Integer latestScore) {
        this.latestScore = latestScore;
    }


    public List<ResumeVersionSummary> getVersions() {
        return versions;
    }


    public void setVersions(
            List<ResumeVersionSummary> versions) {

        this.versions = versions;
    }


    public List<AtsHistorySummary> getAnalyses() {
        return analyses;
    }


    public void setAnalyses(
            List<AtsHistorySummary> analyses) {

        this.analyses = analyses;
    }


    public List<OptimizationHistorySummary>
            getOptimizations() {

        return optimizations;
    }


    public void setOptimizations(
            List<OptimizationHistorySummary> optimizations) {

        this.optimizations =
                optimizations;
    }
}