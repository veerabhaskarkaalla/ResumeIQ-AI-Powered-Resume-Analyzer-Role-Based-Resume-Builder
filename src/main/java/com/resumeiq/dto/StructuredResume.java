package com.resumeiq.dto;

import java.util.ArrayList;
import java.util.List;

public class StructuredResume {

    private String name;

    private String email;

    private String phone;

    private String location;

    private List<String> links =
            new ArrayList<>();

    private String careerObjective;

    private List<EducationEntry> education =
            new ArrayList<>();

    private List<ExperienceEntry> experience =
            new ArrayList<>();

    private List<SkillCategory> skillCategories =
            new ArrayList<>();

    private List<ProjectEntry> projects =
            new ArrayList<>();

    private List<String> researchPublications =
            new ArrayList<>();

    private List<String> certifications =
            new ArrayList<>();

    private List<String> achievements =
            new ArrayList<>();


    public StructuredResume() {
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public List<String> getLinks() {
        return links;
    }


    public void setLinks(
            List<String> links) {

        this.links = links;
    }


    public String getCareerObjective() {
        return careerObjective;
    }


    public void setCareerObjective(
            String careerObjective) {

        this.careerObjective =
                careerObjective;
    }


    public List<EducationEntry> getEducation() {
        return education;
    }


    public void setEducation(
            List<EducationEntry> education) {

        this.education = education;
    }


    public List<ExperienceEntry> getExperience() {
        return experience;
    }


    public void setExperience(
            List<ExperienceEntry> experience) {

        this.experience = experience;
    }


    public List<SkillCategory> getSkillCategories() {
        return skillCategories;
    }


    public void setSkillCategories(
            List<SkillCategory> skillCategories) {

        this.skillCategories =
                skillCategories;
    }


    public List<ProjectEntry> getProjects() {
        return projects;
    }


    public void setProjects(
            List<ProjectEntry> projects) {

        this.projects = projects;
    }


    public List<String> getResearchPublications() {
        return researchPublications;
    }


    public void setResearchPublications(
            List<String> researchPublications) {

        this.researchPublications =
                researchPublications;
    }


    public List<String> getCertifications() {
        return certifications;
    }


    public void setCertifications(
            List<String> certifications) {

        this.certifications =
                certifications;
    }


    public List<String> getAchievements() {
        return achievements;
    }


    public void setAchievements(
            List<String> achievements) {

        this.achievements =
                achievements;
    }
}