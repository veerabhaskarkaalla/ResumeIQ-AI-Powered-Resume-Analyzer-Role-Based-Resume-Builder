package com.resumeiq.dto;

import java.util.ArrayList;
import java.util.List;

public class BulletOptimizationRequest {

    private String entryType;

    private String title;

    private String organization;

    private String duration;

    private String technologies;

    private List<String> originalBullets =
            new ArrayList<>();

    private String targetCompany;

    private String targetRole;

    private String jobDescription;


    public BulletOptimizationRequest() {
    }


    public String getEntryType() {
        return entryType;
    }


    public void setEntryType(
            String entryType) {

        this.entryType = entryType;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(
            String title) {

        this.title = title;
    }


    public String getOrganization() {
        return organization;
    }


    public void setOrganization(
            String organization) {

        this.organization = organization;
    }


    public String getDuration() {
        return duration;
    }


    public void setDuration(
            String duration) {

        this.duration = duration;
    }


    public String getTechnologies() {
        return technologies;
    }


    public void setTechnologies(
            String technologies) {

        this.technologies = technologies;
    }


    public List<String> getOriginalBullets() {
        return originalBullets;
    }


    public void setOriginalBullets(
            List<String> originalBullets) {

        this.originalBullets = originalBullets;
    }


    public String getTargetCompany() {
        return targetCompany;
    }


    public void setTargetCompany(
            String targetCompany) {

        this.targetCompany = targetCompany;
    }


    public String getTargetRole() {
        return targetRole;
    }


    public void setTargetRole(
            String targetRole) {

        this.targetRole = targetRole;
    }


    public String getJobDescription() {
        return jobDescription;
    }


    public void setJobDescription(
            String jobDescription) {

        this.jobDescription = jobDescription;
    }
}