package com.resumeiq.dto;

import java.util.List;

public class DashboardResponse {

    private int totalResumes;

    private int totalAnalyses;

    private int totalVersions;

    private int totalOptimizations;

    private List<DashboardResumeItem> resumes;


    public DashboardResponse() {
    }


    public int getTotalResumes() {
        return totalResumes;
    }


    public void setTotalResumes(int totalResumes) {
        this.totalResumes = totalResumes;
    }


    public int getTotalAnalyses() {
        return totalAnalyses;
    }


    public void setTotalAnalyses(int totalAnalyses) {
        this.totalAnalyses = totalAnalyses;
    }


    public int getTotalVersions() {
        return totalVersions;
    }


    public void setTotalVersions(int totalVersions) {
        this.totalVersions = totalVersions;
    }


    public int getTotalOptimizations() {
        return totalOptimizations;
    }


    public void setTotalOptimizations(
            int totalOptimizations) {

        this.totalOptimizations =
                totalOptimizations;
    }


    public List<DashboardResumeItem> getResumes() {
        return resumes;
    }


    public void setResumes(
            List<DashboardResumeItem> resumes) {

        this.resumes = resumes;
    }
}