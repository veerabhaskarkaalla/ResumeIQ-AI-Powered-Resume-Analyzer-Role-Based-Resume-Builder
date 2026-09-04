package com.resumeiq.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.resumeiq.dto.EducationEntry;
import com.resumeiq.dto.ExperienceEntry;
import com.resumeiq.dto.ProjectEntry;
import com.resumeiq.dto.SkillCategory;
import com.resumeiq.dto.StructuredResume;

@Service
public class ResumeTemplateService {

    public String render(
            StructuredResume resume) {

        if (resume == null) {

            throw new IllegalArgumentException(
                    "Structured resume cannot be null"
            );
        }


        StringBuilder output =
                new StringBuilder();


        // =====================================================
        // HEADER
        // =====================================================

        appendLine(
                output,
                resume.getName()
        );


        List<String> primaryContact =
                new ArrayList<>();


        if (!safe(resume.getEmail()).isBlank()) {

            primaryContact.add(
                    resume.getEmail().trim()
            );
        }


        if (!safe(resume.getPhone()).isBlank()) {

            primaryContact.add(
                    resume.getPhone().trim()
            );
        }


        if (!safe(resume.getLocation()).isBlank()) {

            primaryContact.add(
                    resume.getLocation().trim()
            );
        }


        if (!primaryContact.isEmpty()) {

            appendLine(
                    output,
                    String.join(
                            " | ",
                            primaryContact
                    )
            );
        }


        appendLinks(
                output,
                resume.getLinks()
        );


        output.append("\n");


        // =====================================================
        // CAREER OBJECTIVE
        // =====================================================

        if (!safe(
                resume.getCareerObjective()
        ).isBlank()) {

            appendHeading(
                    output,
                    "CAREER OBJECTIVE"
            );


            appendParagraph(
                    output,
                    resume.getCareerObjective()
            );


            output.append("\n");
        }


        // =====================================================
        // EDUCATION
        // =====================================================

        if (resume.getEducation() != null
                &&
                !resume.getEducation().isEmpty()) {

            appendHeading(
                    output,
                    "EDUCATION"
            );


            for (EducationEntry education :
                    resume.getEducation()) {

                appendEducation(
                        output,
                        education
                );
            }


            output.append("\n");
        }


        // =====================================================
        // WORK EXPERIENCE
        // =====================================================

        if (resume.getExperience() != null
                &&
                !resume.getExperience().isEmpty()) {

            appendHeading(
                    output,
                    "WORK EXPERIENCE"
            );


            for (ExperienceEntry experience :
                    resume.getExperience()) {

                appendExperience(
                        output,
                        experience
                );
            }


            output.append("\n");
        }


        // =====================================================
        // TECHNICAL SKILLS
        // =====================================================

        if (resume.getSkillCategories() != null
                &&
                !resume
                    .getSkillCategories()
                    .isEmpty()) {

            appendHeading(
                    output,
                    "TECHNICAL SKILLS"
            );


            for (SkillCategory category :
                    resume.getSkillCategories()) {

                appendSkillCategory(
                        output,
                        category
                );
            }


            output.append("\n");
        }


        // =====================================================
        // PROJECTS
        // =====================================================

        if (resume.getProjects() != null
                &&
                !resume.getProjects().isEmpty()) {

            appendHeading(
                    output,
                    "PROJECTS"
            );


            for (ProjectEntry project :
                    resume.getProjects()) {

                appendProject(
                        output,
                        project
                );
            }


            output.append("\n");
        }


        // =====================================================
        // RESEARCH PUBLICATION
        // =====================================================

        appendSimpleListSection(
                output,
                "RESEARCH PUBLICATION",
                resume.getResearchPublications()
        );


        // =====================================================
        // CERTIFICATIONS
        // =====================================================

        appendSimpleListSection(
                output,
                "CERTIFICATIONS",
                resume.getCertifications()
        );


        // =====================================================
        // ACHIEVEMENTS
        // =====================================================

        appendSimpleListSection(
                output,
                "ACHIEVEMENTS",
                resume.getAchievements()
        );


        return cleanupOutput(
                output.toString()
        );
    }


    // =========================================================
    // EDUCATION
    // =========================================================

    private void appendEducation(
            StringBuilder output,
            EducationEntry education) {

        if (education == null) {
            return;
        }


        List<String> titleParts =
                new ArrayList<>();


        if (!safe(
                education.getInstitution()
        ).isBlank()) {

            titleParts.add(
                    education
                        .getInstitution()
                        .trim()
            );
        }


        if (!safe(
                education.getDuration()
        ).isBlank()) {

            titleParts.add(
                    education
                        .getDuration()
                        .trim()
            );
        }


        if (!titleParts.isEmpty()) {

            output.append("• ");

            output.append(
                    String.join(
                            " | ",
                            titleParts
                    )
            );

            output.append("\n");
        }


        List<String> secondLine =
                new ArrayList<>();


        if (!safe(
                education.getQualification()
        ).isBlank()) {

            secondLine.add(
                    education
                        .getQualification()
                        .trim()
            );
        }


        if (!safe(
                education.getScore()
        ).isBlank()) {

            secondLine.add(
                    education
                        .getScore()
                        .trim()
            );
        }


        if (!safe(
                education.getLocation()
        ).isBlank()) {

            secondLine.add(
                    education
                        .getLocation()
                        .trim()
            );
        }


        if (!secondLine.isEmpty()) {

            output.append("  ");

            output.append(
                    String.join(
                            " | ",
                            secondLine
                    )
            );

            output.append("\n");
        }
    }


    // =========================================================
    // EXPERIENCE
    // =========================================================

    private void appendExperience(
            StringBuilder output,
            ExperienceEntry experience) {

        if (experience == null) {
            return;
        }


        StringBuilder title =
                new StringBuilder();


        if (!safe(
                experience.getRole()
        ).isBlank()) {

            title.append(
                    experience
                        .getRole()
                        .trim()
            );
        }


        if (!safe(
                experience.getCompany()
        ).isBlank()) {

            if (title.length() > 0) {

                title.append(
                        " - "
                );
            }


            title.append(
                    experience
                        .getCompany()
                        .trim()
            );
        }


        if (!safe(
                experience.getDuration()
        ).isBlank()) {

            if (title.length() > 0) {

                title.append(
                        " | "
                );
            }


            title.append(
                    experience
                        .getDuration()
                        .trim()
            );
        }


        if (title.length() > 0) {

            output.append("• ");

            output.append(
                    title
            );

            output.append("\n");
        }


        if (experience.getBullets()
                != null) {

            for (String bullet :
                    experience.getBullets()) {

                if (safe(bullet)
                        .isBlank()) {

                    continue;
                }


                output.append(
                        "  • "
                );


                output.append(
                        cleanBullet(
                                bullet
                        )
                );


                output.append("\n");
            }
        }
    }


    // =========================================================
    // SKILLS
    // =========================================================

    private void appendSkillCategory(
            StringBuilder output,
            SkillCategory category) {

        if (category == null
                ||
                safe(
                    category.getName()
                ).isBlank()
                ||
                category.getSkills() == null
                ||
                category.getSkills().isEmpty()) {

            return;
        }


        List<String> cleanedSkills =
                new ArrayList<>();


        for (String skill :
                category.getSkills()) {

            if (!safe(skill)
                    .isBlank()) {

                cleanedSkills.add(
                        skill.trim()
                );
            }
        }


        if (cleanedSkills.isEmpty()) {
            return;
        }


        output.append("• ");

        output.append(
                category
                    .getName()
                    .trim()
        );

        output.append(": ");


        output.append(
                String.join(
                        ", ",
                        cleanedSkills
                )
        );


        output.append("\n");
    }


    // =========================================================
    // PROJECT
    // =========================================================

    private void appendProject(
            StringBuilder output,
            ProjectEntry project) {

        if (project == null) {
            return;
        }


        StringBuilder title =
                new StringBuilder();


        if (!safe(
                project.getName()
        ).isBlank()) {

            title.append(
                    project
                        .getName()
                        .trim()
            );
        }


        if (!safe(
                project.getTechnologies()
        ).isBlank()) {

            if (title.length() > 0) {

                title.append(
                        " | "
                );
            }


            title.append(
                    project
                        .getTechnologies()
                        .trim()
            );
        }


        if (title.length() > 0) {

            output.append("• ");

            output.append(
                    title
            );

            output.append("\n");
        }


        if (project.getBullets()
                != null) {

            for (String bullet :
                    project.getBullets()) {

                if (safe(bullet)
                        .isBlank()) {

                    continue;
                }


                output.append(
                        "  • "
                );


                output.append(
                        cleanBullet(
                                bullet
                        )
                );


                output.append("\n");
            }
        }
    }


    // =========================================================
    // SIMPLE LIST
    // =========================================================

    private void appendSimpleListSection(
            StringBuilder output,
            String heading,
            List<String> values) {

        if (values == null
                ||
                values.isEmpty()) {

            return;
        }


        boolean hasValue =
                false;


        for (String value : values) {

            if (!safe(value).isBlank()) {

                hasValue = true;
                break;
            }
        }


        if (!hasValue) {
            return;
        }


        appendHeading(
                output,
                heading
        );


        for (String value : values) {

            if (safe(value).isBlank()) {

                continue;
            }


            output.append("• ");

            output.append(
                    cleanBullet(
                            value
                    )
            );

            output.append("\n");
        }


        output.append("\n");
    }


    // =========================================================
    // HEADER LINKS
    // =========================================================

    private void appendLinks(
            StringBuilder output,
            List<String> links) {

        if (links == null
                ||
                links.isEmpty()) {

            return;
        }


        List<String> cleanLinks =
                new ArrayList<>();


        for (String link : links) {

            if (!safe(link).isBlank()) {

                cleanLinks.add(
                        link.trim()
                );
            }
        }


        /*
         * 2 links per line.
         *
         * LinkedIn | GitHub
         * LeetCode | HackerRank
         */
        for (int i = 0;
             i < cleanLinks.size();
             i += 2) {

            if (i + 1
                    < cleanLinks.size()) {

                appendLine(
                        output,
                        cleanLinks.get(i)
                        + " | "
                        + cleanLinks.get(i + 1)
                );

            } else {

                appendLine(
                        output,
                        cleanLinks.get(i)
                );
            }
        }
    }


    // =========================================================
    // COMMON
    // =========================================================

    private void appendHeading(
            StringBuilder output,
            String heading) {

        output.append(
                heading
        );

        output.append("\n");
    }


    private void appendParagraph(
            StringBuilder output,
            String value) {

        if (safe(value).isBlank()) {

            return;
        }


        output.append(
                value
                    .replaceAll(
                        "\\s+",
                        " "
                    )
                    .trim()
        );


        output.append("\n");
    }


    private void appendLine(
            StringBuilder output,
            String value) {

        if (safe(value).isBlank()) {

            return;
        }


        output.append(
                value.trim()
        );


        output.append("\n");
    }


    private String cleanBullet(
            String value) {

        if (value == null) {

            return "";
        }


        return value
                .trim()
                .replaceFirst(
                        "^[•●▪◦*-]\\s*",
                        ""
                )
                .trim();
    }


    private String cleanupOutput(
            String value) {

        if (value == null) {

            return "";
        }


        return value
                .replaceAll(
                        "(\\R){3,}",
                        "\n\n"
                )
                .trim();
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}