package com.resumeiq.dto;

public class JobDescriptionRequest {

    private String company;
    private String role;
    private String jobDescription;

    public JobDescriptionRequest() {
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

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(
            String jobDescription) {

        this.jobDescription =
                jobDescription;
    }
}