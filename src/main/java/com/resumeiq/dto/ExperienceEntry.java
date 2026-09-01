package com.resumeiq.dto;

import java.util.ArrayList;
import java.util.List;

public class ExperienceEntry {

    private String role;

    private String company;

    private String duration;

    private List<String> bullets =
            new ArrayList<>();


    public ExperienceEntry() {
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public String getCompany() {
        return company;
    }


    public void setCompany(String company) {
        this.company = company;
    }


    public String getDuration() {
        return duration;
    }


    public void setDuration(String duration) {
        this.duration = duration;
    }


    public List<String> getBullets() {
        return bullets;
    }


    public void setBullets(
            List<String> bullets) {

        this.bullets = bullets;
    }
}