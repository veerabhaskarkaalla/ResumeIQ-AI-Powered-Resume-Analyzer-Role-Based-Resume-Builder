package com.resumeiq.dto;

import java.util.ArrayList;
import java.util.List;

public class ProjectEntry {

    private String name;

    private String technologies;

    private List<String> bullets =
            new ArrayList<>();


    public ProjectEntry() {
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getTechnologies() {
        return technologies;
    }


    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }


    public List<String> getBullets() {
        return bullets;
    }


    public void setBullets(
            List<String> bullets) {

        this.bullets = bullets;
    }
}