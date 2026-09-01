package com.resumeiq.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class SkillCatalog {

    private static final Map<String, List<String>>
            SKILLS =
            new LinkedHashMap<>();


    static {

        // ==============================
        // PROGRAMMING
        // ==============================

        add(
                "Java",
                "java"
        );

        add(
                "Python",
                "python"
        );

        add(
                "C++",
                "c++",
                "cpp"
        );

        add(
                "JavaScript",
                "javascript",
                "js"
        );

        add(
                "TypeScript",
                "typescript"
        );

        add(
                "SQL",
                "sql"
        );


        // ==============================
        // FRONTEND
        // ==============================

        add(
                "React",
                "react",
                "react.js",
                "reactjs"
        );

        add(
                "Angular",
                "angular"
        );

        add(
                "HTML",
                "html",
                "html5"
        );

        add(
                "CSS",
                "css",
                "css3"
        );


        // ==============================
        // JAVA / BACKEND
        // ==============================

        add(
                "Spring Boot",
                "spring boot",
                "springboot"
        );

        add(
                "Hibernate",
                "hibernate"
        );

        add(
                "JPA",
                "jpa",
                "java persistence api"
        );

        add(
                "REST API",
                "rest api",
                "rest apis",
                "restful api",
                "restful apis",
                "rest services"
        );

        add(
                "Microservices",
                "microservices",
                "microservice architecture"
        );

        add(
                "Flask",
                "flask"
        );

        add(
                "Django",
                "django"
        );


        // ==============================
        // DATABASES
        // ==============================

        add(
                "MySQL",
                "mysql"
        );

        add(
                "PostgreSQL",
                "postgresql",
                "postgres"
        );

        add(
                "MongoDB",
                "mongodb",
                "mongo db"
        );

        add(
                "Redis",
                "redis"
        );


        // ==============================
        // AWS
        // ==============================

        add(
                "AWS",
                "aws",
                "amazon web services"
        );

        add(
                "Amazon EC2",
                "amazon ec2",
                "aws ec2",
                "ec2"
        );

        add(
                "Amazon S3",
                "amazon s3",
                "aws s3",
                "s3"
        );

        add(
                "AWS Lambda",
                "aws lambda",
                "amazon lambda",
                "lambda"
        );

        add(
                "Amazon RDS",
                "amazon rds",
                "aws rds",
                "rds"
        );

        add(
                "Amazon DynamoDB",
                "amazon dynamodb",
                "aws dynamodb",
                "dynamodb"
        );

        add(
                "AWS IAM",
                "aws iam",
                "amazon iam",
                "iam"
        );

        add(
                "Amazon VPC",
                "amazon vpc",
                "aws vpc",
                "vpc"
        );

        add(
                "Amazon CloudWatch",
                "amazon cloudwatch",
                "aws cloudwatch",
                "cloudwatch"
        );

        add(
                "AWS CloudFormation",
                "aws cloudformation",
                "amazon cloudformation",
                "cloudformation"
        );


        // ==============================
        // DEVOPS
        // ==============================

        add(
                "Docker",
                "docker"
        );

        add(
                "Kubernetes",
                "kubernetes",
                "k8s"
        );

        add(
                "Terraform",
                "terraform"
        );

        add(
                "Git",
                "git"
        );

        add(
                "GitHub",
                "github",
                "git hub"
        );

        add(
                "Jenkins",
                "jenkins"
        );

        add(
                "CI/CD",
                "ci/cd",
                "ci cd",
                "continuous integration",
                "continuous deployment",
                "continuous delivery"
        );

        add(
                "Linux",
                "linux"
        );


        // ==============================
        // TESTING / BUILD
        // ==============================

        add(
                "JUnit",
                "junit"
        );

        add(
                "Selenium",
                "selenium"
        );

        add(
                "Maven",
                "maven"
        );

        add(
                "Gradle",
                "gradle"
        );


        // ==============================
        // DATA / ML
        // ==============================

        add(
                "Machine Learning",
                "machine learning",
                "ml"
        );

        add(
                "Deep Learning",
                "deep learning"
        );

        add(
                "Pandas",
                "pandas"
        );

        add(
                "NumPy",
                "numpy"
        );

        add(
                "Matplotlib",
                "matplotlib"
        );

        add(
                "scikit-learn",
                "scikit-learn",
                "sklearn"
        );


        // ==============================
        // DISTRIBUTED SYSTEMS
        // ==============================

        add(
                "Kafka",
                "kafka",
                "apache kafka"
        );
    }


    private SkillCatalog() {
    }


    private static void add(
            String canonical,
            String... aliases) {

        List<String> values =
                new ArrayList<>();


        values.add(
                canonical
        );


        for (String alias : aliases) {

            values.add(
                    alias
            );
        }


        SKILLS.put(
                canonical,
                values
        );
    }


    // =========================================================
    // FIND ALL SKILLS
    // =========================================================

    public static List<String> findSkills(
            String text) {

        Set<String> result =
                new LinkedHashSet<>();


        if (text == null
                || text.isBlank()) {

            return new ArrayList<>();
        }


        for (
                Map.Entry<String, List<String>> entry
                : SKILLS.entrySet()
        ) {

            for (String alias :
                    entry.getValue()) {

                if (containsAlias(
                        text,
                        alias
                )) {

                    result.add(
                            entry.getKey()
                    );

                    break;
                }
            }
        }


        return new ArrayList<>(
                result
        );
    }


    // =========================================================
    // CHECK ONE SKILL
    // =========================================================

    public static boolean containsSkill(
            String text,
            String skill) {

        if (text == null
                || skill == null
                || skill.isBlank()) {

            return false;
        }


        String canonical =
                resolveCanonicalSkill(
                        skill
                );


        /*
         * Known catalog skill.
         */
        if (canonical != null) {

            List<String> aliases =
                    SKILLS.get(
                            canonical
                    );


            for (String alias :
                    aliases) {

                if (containsAlias(
                        text,
                        alias
                )) {

                    return true;
                }
            }


            return false;
        }


        /*
         * Unknown AI/JD phrase:
         * fallback to safe phrase matching.
         */
        return containsAlias(
                text,
                skill
        );
    }


    // =========================================================
    // CANONICAL NAME
    // =========================================================

    public static String resolveCanonicalSkill(
            String skill) {

        if (skill == null
                || skill.isBlank()) {

            return null;
        }


        String normalizedInput =
                normalize(
                        skill
                );


        for (
                Map.Entry<String, List<String>> entry
                : SKILLS.entrySet()
        ) {

            if (
                    normalize(
                            entry.getKey()
                    ).equals(
                            normalizedInput
                    )
            ) {

                return entry.getKey();
            }


            for (String alias :
                    entry.getValue()) {

                if (
                        normalize(
                                alias
                        ).equals(
                                normalizedInput
                        )
                ) {

                    return entry.getKey();
                }
            }
        }


        return null;
    }


    // =========================================================
    // REGEX
    // =========================================================

    private static boolean containsAlias(
            String text,
            String alias) {

        if (text == null
                || alias == null
                || alias.isBlank()) {

            return false;
        }


        String regex =
                "(?i)"
                +
                "(?<![A-Za-z0-9])"
                +
                Pattern.quote(
                        alias.trim()
                )
                +
                "(?![A-Za-z0-9])";


        return Pattern
                .compile(regex)
                .matcher(text)
                .find();
    }


    private static String normalize(
            String value) {

        if (value == null) {

            return "";
        }


        return value
                .toLowerCase(
                        Locale.ROOT
                )
                .replaceAll(
                        "[^a-z0-9+#.]+",
                        " "
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }
}