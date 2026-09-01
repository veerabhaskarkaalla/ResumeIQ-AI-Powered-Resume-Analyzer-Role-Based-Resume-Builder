package com.resumeiq.dto;

public class EducationEntry {

    private String institution;

    private String duration;

    private String qualification;

    private String score;

    private String location;


    public EducationEntry() {
    }


    public String getInstitution() {
        return institution;
    }


    public void setInstitution(String institution) {
        this.institution = institution;
    }


    public String getDuration() {
        return duration;
    }


    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getQualification() {
        return qualification;
    }


    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


    public String getScore() {
        return score;
    }


    public void setScore(String score) {
        this.score = score;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }
}