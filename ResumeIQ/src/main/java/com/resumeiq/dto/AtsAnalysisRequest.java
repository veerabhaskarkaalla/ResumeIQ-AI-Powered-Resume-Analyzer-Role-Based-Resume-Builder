package com.resumeiq.dto;

public class AtsAnalysisRequest {

    private Long resumeId;
    private String company;
    private String role;
    private String jobDescription;

    public AtsAnalysisRequest() {
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

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(
            String jobDescription) {

        this.jobDescription =
                jobDescription;
    }
}