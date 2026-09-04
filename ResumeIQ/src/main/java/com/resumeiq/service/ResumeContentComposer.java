package com.resumeiq.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.resumeiq.document.StructuredResumeParser;
import com.resumeiq.dto.ResumeOptimizationResult;
import com.resumeiq.dto.SkillCategory;
import com.resumeiq.dto.StructuredResume;
import com.resumeiq.util.SkillCatalog;

@Service
public class ResumeContentComposer {

    private final StructuredResumeParser
            structuredResumeParser;

    private final ResumeTemplateService
            templateService;


    public ResumeContentComposer(
            StructuredResumeParser structuredResumeParser,
            ResumeTemplateService templateService) {

        this.structuredResumeParser =
                structuredResumeParser;

        this.templateService =
                templateService;
    }


    public String compose(
            String originalText,
            ResumeOptimizationResult optimization) {

        if (originalText == null
                ||
                originalText.isBlank()) {

            throw new IllegalArgumentException(
                    "Original resume content is empty"
            );
        }


        StructuredResume structured =
                structuredResumeParser.parse(
                        originalText
                );


        /*
         * PHASE 17A:
         *
         * Structure first.
         *
         * We intentionally do NOT randomly replace
         * experience/project bullets here anymore.
         *
         * 17B lo each ExperienceEntry / ProjectEntry
         * separately evidence-grounded AI optimization
         * chestham.
         */


        applyCareerObjective(
                structured,
                optimization
        );


        applyVerifiedSkills(
                structured,
                originalText,
                optimization
        );


        return templateService.render(
                structured
        );
    }


    // =========================================================
    // CAREER OBJECTIVE
    // =========================================================

    private void applyCareerObjective(
            StructuredResume resume,
            ResumeOptimizationResult optimization) {

        if (optimization == null) {

            return;
        }


        String optimized =
                safe(
                        optimization
                            .getOptimizedSummary()
                );


        if (optimized.isBlank()) {

            return;
        }


        resume.setCareerObjective(
                optimized
                    .replaceAll(
                        "\\s+",
                        " "
                    )
                    .trim()
        );
    }


    // =========================================================
    // VERIFIED SKILLS
    // =========================================================

    private void applyVerifiedSkills(
            StructuredResume resume,
            String originalText,
            ResumeOptimizationResult optimization) {

        if (optimization == null
                ||
                optimization
                    .getSkillsToHighlight()
                    == null) {

            return;
        }


        if (resume.getSkillCategories()
                == null) {

            resume.setSkillCategories(
                    new ArrayList<>()
            );
        }


        for (String requestedSkill :
                optimization
                    .getSkillsToHighlight()) {

            if (requestedSkill == null
                    ||
                    requestedSkill.isBlank()) {

                continue;
            }


            /*
             * Important:
             * Original resume evidence required.
             */
            if (!SkillCatalog.containsSkill(
                    originalText,
                    requestedSkill
            )) {

                continue;
            }


            String canonical =
                    SkillCatalog
                            .resolveCanonicalSkill(
                                    requestedSkill
                            );


            String skill =
                    canonical == null
                            ? requestedSkill.trim()
                            : canonical;


            if (alreadyExists(
                    resume,
                    skill)) {

                continue;
            }


            String categoryName =
                    findCategory(
                            skill
                    );


            SkillCategory category =
                    findOrCreateCategory(
                            resume,
                            categoryName
                    );


            category.getSkills()
                    .add(
                            skill
                    );
        }
    }


    private boolean alreadyExists(
            StructuredResume resume,
            String skill) {

        if (resume.getSkillCategories()
                == null) {

            return false;
        }


        String targetCanonical =
                SkillCatalog
                        .resolveCanonicalSkill(
                                skill
                        );


        String target =
                targetCanonical == null
                        ? skill
                        : targetCanonical;


        for (SkillCategory category :
                resume.getSkillCategories()) {

            if (category == null
                    ||
                    category.getSkills() == null) {

                continue;
            }


            for (String existing :
                    category.getSkills()) {

                String existingCanonical =
                        SkillCatalog
                                .resolveCanonicalSkill(
                                        existing
                                );


                String value =
                        existingCanonical == null
                                ? existing
                                : existingCanonical;


                if (value != null
                        &&
                        value.equalsIgnoreCase(
                                target
                        )) {

                    return true;
                }
            }
        }


        return false;
    }


    private SkillCategory findOrCreateCategory(
            StructuredResume resume,
            String categoryName) {

        for (SkillCategory category :
                resume.getSkillCategories()) {

            if (category != null
                    &&
                    category.getName() != null
                    &&
                    category
                        .getName()
                        .equalsIgnoreCase(
                            categoryName
                        )) {

                if (category.getSkills()
                        == null) {

                    category.setSkills(
                            new ArrayList<>()
                    );
                }


                return category;
            }
        }


        SkillCategory created =
                new SkillCategory();


        created.setName(
                categoryName
        );


        created.setSkills(
                new ArrayList<>()
        );


        resume.getSkillCategories()
                .add(
                        created
                );


        return created;
    }


    private String findCategory(
            String skill) {

        if (skill == null) {

            return "Libraries & Tools";
        }


        return switch (skill) {

            case "Java",
                 "Python",
                 "C++",
                 "JavaScript",
                 "TypeScript",
                 "SQL"
                    -> "Programming Languages";


            case "React",
                 "Angular",
                 "HTML",
                 "CSS",
                 "Spring Boot",
                 "Hibernate",
                 "JPA",
                 "REST API",
                 "Microservices",
                 "Flask",
                 "Django"
                    -> "Web & Backend";


            case "MySQL",
                 "PostgreSQL",
                 "MongoDB",
                 "Redis"
                    -> "Databases";


            case "AWS",
                 "Amazon EC2",
                 "Amazon S3",
                 "AWS Lambda",
                 "Amazon RDS",
                 "Amazon DynamoDB",
                 "AWS IAM",
                 "Amazon VPC",
                 "Amazon CloudWatch",
                 "AWS CloudFormation"
                    -> "Cloud & AWS";


            case "Machine Learning",
                 "Deep Learning",
                 "Pandas",
                 "NumPy",
                 "Matplotlib",
                 "scikit-learn"
                    -> "Libraries & Tools";


            case "Docker",
                 "Kubernetes",
                 "Terraform",
                 "Git",
                 "GitHub",
                 "Jenkins",
                 "CI/CD",
                 "Linux",
                 "JUnit",
                 "Selenium",
                 "Maven",
                 "Gradle",
                 "Kafka"
                    -> "Libraries & Tools";


            default
                    -> "Core Concepts";
        };
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}