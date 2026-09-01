package com.resumeiq.ats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.resumeiq.dto.JobDescriptionAnalysis;
import com.resumeiq.dto.JobDescriptionRequest;
import com.resumeiq.util.SkillCatalog;

@Component
public class JobDescriptionAnalyzer {

    private static final List<String>
            IMPORTANT_KEYWORDS =
            Arrays.asList(

                    "agile",
                    "scrum",
                    "ci/cd",
                    "continuous integration",
                    "continuous deployment",

                    "system design",
                    "distributed systems",

                    "data structures",
                    "algorithms",

                    "unit testing",
                    "integration testing",

                    "object-oriented programming",
                    "oop",

                    "version control",

                    "cloud",
                    "cloud services",

                    "backend",
                    "frontend",

                    "database",
                    "data pipelines",

                    "scalable",
                    "high availability"
            );

    public JobDescriptionAnalysis analyze(
            JobDescriptionRequest request) {

        if (request == null
                || request.getJobDescription() == null
                || request.getJobDescription().isBlank()) {

            throw new IllegalArgumentException(
                    "Job description cannot be empty"
            );
        }

        String jd =
                request.getJobDescription();

        List<String> allSkills =
                SkillCatalog.findSkills(jd);

        List<String> preferredSkills =
                extractPreferredSkills(jd);

        List<String> requiredSkills =
                new ArrayList<>(allSkills);

        requiredSkills.removeAll(
                preferredSkills
        );

        if (requiredSkills.isEmpty()
                && !allSkills.isEmpty()) {

            requiredSkills =
                    new ArrayList<>(allSkills);
        }

        Set<String> keywords =
                new LinkedHashSet<>();

        keywords.addAll(allSkills);

        String lowerJD =
                jd.toLowerCase();

        for (String keyword :
                IMPORTANT_KEYWORDS) {

            if (lowerJD.contains(
                    keyword.toLowerCase())) {

                keywords.add(keyword);
            }
        }

        JobDescriptionAnalysis analysis =
                new JobDescriptionAnalysis();

        analysis.setCompany(
                request.getCompany()
        );

        analysis.setRole(
                request.getRole()
        );

        analysis.setRequiredSkills(
                requiredSkills
        );

        analysis.setPreferredSkills(
                preferredSkills
        );

        analysis.setKeywords(
                new ArrayList<>(keywords)
        );

        analysis.setMinimumExperienceYears(
                extractExperienceYears(jd)
        );

        return analysis;
    }

    private List<String>
            extractPreferredSkills(
                    String jd) {

        Set<String> preferred =
                new LinkedHashSet<>();

        String[] sentences =
                jd.split(
                    "(?<=[.!?])\\s+|\\R+"
                );

        for (String sentence :
                sentences) {

            String lower =
                    sentence.toLowerCase();

            boolean preferredSentence =
                    lower.contains("preferred")
                    || lower.contains("nice to have")
                    || lower.contains("good to have")
                    || lower.contains("plus")
                    || lower.contains("bonus");

            if (preferredSentence) {

                preferred.addAll(
                        SkillCatalog.findSkills(
                                sentence
                        )
                );
            }
        }

        return new ArrayList<>(preferred);
    }

    private int extractExperienceYears(
            String jd) {

        Pattern pattern =
                Pattern.compile(
                    "(?i)(\\d+)\\s*\\+?\\s*"
                    + "(?:years?|yrs?)"
                );

        Matcher matcher =
                pattern.matcher(jd);

        int maximumYears = 0;

        while (matcher.find()) {

            int years =
                    Integer.parseInt(
                            matcher.group(1)
                    );

            maximumYears =
                    Math.max(
                            maximumYears,
                            years
                    );
        }

        return maximumYears;
    }
}