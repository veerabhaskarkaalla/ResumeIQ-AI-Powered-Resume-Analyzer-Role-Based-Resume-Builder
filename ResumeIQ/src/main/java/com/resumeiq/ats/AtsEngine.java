package com.resumeiq.ats;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.resumeiq.dto.AtsAnalysisResult;
import com.resumeiq.dto.JobDescriptionAnalysis;
import com.resumeiq.dto.ParsedResume;
import com.resumeiq.util.SkillCatalog;

@Component
public class AtsEngine {

    public AtsAnalysisResult analyze(
            Long resumeId,
            String rawResumeText,
            ParsedResume resume,
            JobDescriptionAnalysis job) {

        if (rawResumeText == null) {
            rawResumeText = "";
        }

        List<String> allJobSkills =
                new ArrayList<>();

        allJobSkills.addAll(
                job.getRequiredSkills()
        );

        for (String skill :
                job.getPreferredSkills()) {

            if (!allJobSkills.contains(skill)) {
                allJobSkills.add(skill);
            }
        }

        List<String> matchedSkills =
                new ArrayList<>();

        List<String> missingSkills =
                new ArrayList<>();

        for (String skill :
                allJobSkills) {

            if (SkillCatalog.containsSkill(
                    rawResumeText,
                    skill)) {

                matchedSkills.add(skill);

            } else {

                missingSkills.add(skill);
            }
        }

        int skillsMatch =
                calculateSkillMatch(
                        rawResumeText,
                        job
                );

        List<String> missingKeywords =
                new ArrayList<>();

        int keywordMatch =
                calculateKeywordMatch(
                        rawResumeText,
                        job.getKeywords(),
                        missingKeywords
                );

        int experienceRelevance =
                calculateSectionRelevance(
                        resume.getExperience(),
                        allJobSkills
                );

        int projectsRelevance =
                calculateSectionRelevance(
                        resume.getProjects(),
                        allJobSkills
                );

        int educationFit =
                isNotBlank(
                        resume.getEducation()
                )
                ? 100
                : 0;

        int structure =
                calculateStructure(resume);

        int formatting =
                calculateFormatting(
                        rawResumeText,
                        resume
                );

        int quantification =
                calculateQuantification(
                        resume
                );

        int overallScore =
                (int) Math.round(

                        skillsMatch * 0.25

                        + keywordMatch * 0.20

                        + experienceRelevance * 0.15

                        + projectsRelevance * 0.10

                        + educationFit * 0.05

                        + structure * 0.10

                        + formatting * 0.05

                        + quantification * 0.10
                );

        AtsAnalysisResult result =
                new AtsAnalysisResult();

        result.setResumeId(resumeId);

        result.setCompany(
                job.getCompany()
        );

        result.setRole(
                job.getRole()
        );

        result.setOverallScore(
                clamp(overallScore)
        );

        result.setKeywordMatch(
                keywordMatch
        );

        result.setSkillsMatch(
                skillsMatch
        );

        result.setExperienceRelevance(
                experienceRelevance
        );

        result.setProjectsRelevance(
                projectsRelevance
        );

        result.setEducationFit(
                educationFit
        );

        result.setStructure(
                structure
        );

        result.setFormatting(
                formatting
        );

        result.setQuantification(
                quantification
        );

        result.setMatchedSkills(
                matchedSkills
        );

        result.setMissingSkills(
                missingSkills
        );

        result.setMissingKeywords(
                missingKeywords
        );

        result.setRecommendation(
                buildRecommendation(
                        overallScore,
                        missingSkills,
                        missingKeywords
                )
        );

        return result;
    }

    private int calculateSkillMatch(
            String resumeText,
            JobDescriptionAnalysis job) {

        List<String> targetSkills =
                job.getRequiredSkills();

        if (targetSkills == null
                || targetSkills.isEmpty()) {

            targetSkills =
                    job.getPreferredSkills();
        }

        if (targetSkills == null
                || targetSkills.isEmpty()) {

            return 0;
        }

        int matched = 0;

        for (String skill :
                targetSkills) {

            if (SkillCatalog.containsSkill(
                    resumeText,
                    skill)) {

                matched++;
            }
        }

        return percentage(
                matched,
                targetSkills.size()
        );
    }

    private int calculateKeywordMatch(
            String resumeText,
            List<String> keywords,
            List<String> missingKeywords) {

        if (keywords == null
                || keywords.isEmpty()) {

            return 0;
        }

        int matched = 0;

        String lowerResume =
                resumeText.toLowerCase();

        for (String keyword :
                keywords) {

            boolean found;

            if (SkillCatalog.containsSkill(
                    resumeText,
                    keyword)) {

                found = true;

            } else {

                found =
                        lowerResume.contains(
                                keyword.toLowerCase()
                        );
            }

            if (found) {

                matched++;

            } else {

                missingKeywords.add(
                        keyword
                );
            }
        }

        return percentage(
                matched,
                keywords.size()
        );
    }

    private int calculateSectionRelevance(
            String section,
            List<String> targetSkills) {

        if (!isNotBlank(section)) {
            return 0;
        }

        if (targetSkills == null
                || targetSkills.isEmpty()) {

            return 50;
        }

        int matches = 0;

        for (String skill :
                targetSkills) {

            if (SkillCatalog.containsSkill(
                    section,
                    skill)) {

                matches++;
            }
        }

        if (matches == 0) {
            return 25;
        }

        int score =
                40 + percentage(
                        matches,
                        targetSkills.size()
                );

        return Math.min(
                score,
                100
        );
    }

    private int calculateStructure(
            ParsedResume resume) {

        int present = 0;
        int total = 5;

        if (isNotBlank(resume.getSummary())) {
            present++;
        }

        if (resume.getSkills() != null
                && !resume.getSkills().isEmpty()) {

            present++;
        }

        if (isNotBlank(
                resume.getExperience())) {

            present++;
        }

        if (isNotBlank(
                resume.getProjects())) {

            present++;
        }

        if (isNotBlank(
                resume.getEducation())) {

            present++;
        }

        return percentage(
                present,
                total
        );
    }

    private int calculateFormatting(
            String text,
            ParsedResume resume) {

        int score = 100;

        if (text.length() < 250) {
            score -= 30;
        }

        if (text.length() > 15000) {
            score -= 20;
        }

        int veryLongLines = 0;

        for (String line :
                text.split("\\R")) {

            if (line.length() > 180) {
                veryLongLines++;
            }
        }

        if (veryLongLines >= 3) {
            score -= 20;
        }

        if (!isNotBlank(
                resume.getEmail())) {

            score -= 15;
        }

        if (!isNotBlank(
                resume.getPhone())) {

            score -= 15;
        }

        return clamp(score);
    }

    private int calculateQuantification(
            ParsedResume resume) {

        String text =
                safe(resume.getExperience())
                + "\n"
                + safe(resume.getProjects());

        Pattern pattern =
                Pattern.compile(
                    "(?i)(\\d+\\s*%|"
                    + "\\$\\s?\\d+|"
                    + "\\d+\\+|"
                    + "\\d+x|"
                    + "\\d+)"
                );

        Matcher matcher =
                pattern.matcher(text);

        Set<String> matches =
                new LinkedHashSet<>();

        while (matcher.find()) {

            matches.add(
                    matcher.group()
            );
        }

        return Math.min(
                100,
                matches.size() * 20
        );
    }

    private String buildRecommendation(
            int overallScore,
            List<String> missingSkills,
            List<String> missingKeywords) {

        if (overallScore >= 85) {

            return "Strong match. Focus on small keyword "
                    + "and achievement improvements.";
        }

        if (overallScore >= 70) {

            return "Good match. Improve missing skills, "
                    + "job-specific keywords and quantified "
                    + "achievements.";
        }

        if (overallScore >= 50) {

            return "Moderate match. Resume needs stronger "
                    + "JD alignment, relevant project evidence "
                    + "and better keywords.";
        }

        if (!missingSkills.isEmpty()
                || !missingKeywords.isEmpty()) {

            return "Low match. Build evidence for missing "
                    + "requirements before adding them to "
                    + "the resume.";
        }

        return "Resume needs significant improvement "
                + "for this role.";
    }

    private boolean isNotBlank(
            String value) {

        return value != null
                && !value.isBlank();
    }

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    private int percentage(
            int matched,
            int total) {

        if (total <= 0) {
            return 0;
        }

        return clamp(
                (int) Math.round(
                        matched * 100.0 / total
                )
        );
    }

    private int clamp(int value) {

        return Math.max(
                0,
                Math.min(100, value)
        );
    }
}