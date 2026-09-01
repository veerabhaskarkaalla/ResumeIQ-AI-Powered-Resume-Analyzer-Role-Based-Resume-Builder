package com.resumeiq.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.resumeiq.dto.EducationEntry;
import com.resumeiq.dto.ExperienceEntry;
import com.resumeiq.dto.ProjectEntry;
import com.resumeiq.dto.SkillCategory;
import com.resumeiq.dto.StructuredResume;

@Component
public class StructuredResumeParser {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
            );


    private static final Pattern PHONE_PATTERN =
            Pattern.compile(
                    "(?:\\+91[\\s-]?)?[6-9]\\d{9}"
            );


    private static final Pattern URL_PATTERN =
            Pattern.compile(
                    "(?i)(?:https?://)?(?:www\\.)?"
                    + "(?:linkedin\\.com|github\\.com|leetcode\\.com|hackerrank\\.com)"
                    + "/[^\\s|]+"
            );


    private static final Pattern YEAR_RANGE_PATTERN =
            Pattern.compile(
                    "(?i)"
                    + "(?:"
                    + "(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)"
                    + "\\s+)?"
                    + "(?:19|20)\\d{2}"
                    + "\\s*[–—-]\\s*"
                    + "(?:"
                    + "(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)"
                    + "\\s+)?"
                    + "(?:(?:19|20)\\d{2}|Present)"
            );


    private static final Pattern SCORE_PATTERN =
            Pattern.compile(
                    "(?i)"
                    + "(?:CGPA|GPA)\\s*:?\\s*[0-9.]+"
                    + "|"
                    + "\\b\\d{1,3}(?:\\.\\d+)?%"
            );


    private static final Pattern QUALIFICATION_PATTERN =
            Pattern.compile(
                    "(?i)\\b("
                    + "B\\.?\\s*Tech"
                    + "|Bachelor"
                    + "|Diploma"
                    + "|Secondary School Certificate"
                    + "|SSC"
                    + "|B\\.?E\\.?"
                    + "|M\\.?\\s*Tech"
                    + "|Master"
                    + "|Intermediate"
                    + "|HSC"
                    + "|10th"
                    + "|12th"
                    + ")\\b"
            );


    private static final Pattern LOCATION_AT_END =
            Pattern.compile(
                    "(?:\\s*[–—-]\\s*|\\s+)"
                    + "([A-Z][A-Za-z .']{1,35},\\s*"
                    + "[A-Z][A-Za-z .']{1,35})$"
            );


    private static final List<String> HEADINGS =
            Arrays.asList(
                    "career objective",
                    "professional summary",
                    "summary",
                    "objective",

                    "education",
                    "academics",
                    "academic qualifications",

                    "work experience",
                    "experience",
                    "professional experience",
                    "internship",
                    "internships",

                    "technical skills",
                    "skills",
                    "core skills",
                    "technical expertise",

                    "projects",
                    "project experience",
                    "academic projects",
                    "personal projects",

                    "research publication",
                    "research publications",
                    "publication",
                    "publications",
                    "research",

                    "certifications",
                    "certification",
                    "certificates",

                    "achievements",
                    "awards"
            );


    public StructuredResume parse(
            String rawText) {

        if (rawText == null
                ||
            rawText.isBlank()) {

            throw new IllegalArgumentException(
                    "Resume text cannot be empty"
            );
        }


        String text =
                normalizeText(
                        rawText
                );


        StructuredResume resume =
                new StructuredResume();


        parseHeader(
                text,
                resume
        );


        resume.setCareerObjective(
                paragraph(
                        extractSection(
                                text,
                                "career objective",
                                "professional summary",
                                "summary",
                                "objective"
                        )
                )
        );


        resume.setEducation(
                parseEducation(
                        extractSection(
                                text,
                                "education",
                                "academics",
                                "academic qualifications"
                        )
                )
        );


        resume.setExperience(
                parseExperience(
                        extractSection(
                                text,
                                "work experience",
                                "experience",
                                "professional experience",
                                "internship",
                                "internships"
                        )
                )
        );


        resume.setSkillCategories(
                parseSkills(
                        extractSection(
                                text,
                                "technical skills",
                                "skills",
                                "core skills",
                                "technical expertise"
                        )
                )
        );


        resume.setProjects(
                parseProjects(
                        extractSection(
                                text,
                                "projects",
                                "project experience",
                                "academic projects",
                                "personal projects"
                        )
                )
        );


        resume.setResearchPublications(
                parseSimpleBullets(
                        extractSection(
                                text,
                                "research publication",
                                "research publications",
                                "publication",
                                "publications",
                                "research"
                        )
                )
        );


        resume.setCertifications(
                parseSimpleBullets(
                        extractSection(
                                text,
                                "certifications",
                                "certification",
                                "certificates"
                        )
                )
        );


        resume.setAchievements(
                parseSimpleBullets(
                        extractSection(
                                text,
                                "achievements",
                                "awards"
                        )
                )
        );


        return resume;
    }


    // =========================================================
    // HEADER
    // =========================================================

    private void parseHeader(
            String text,
            StructuredResume resume) {

        String[] lines =
                text.split("\\R");


        List<String> headerLines =
                new ArrayList<>();


        for (String raw :
                lines) {

            if (isHeading(raw)) {
                break;
            }


            String line =
                    cleanup(raw);


            if (!line.isBlank()) {

                headerLines.add(
                        line
                );
            }
        }


        if (!headerLines.isEmpty()) {

            resume.setName(
                    cleanHeaderName(
                            headerLines.get(0)
                    )
            );
        }


        Matcher emailMatcher =
                EMAIL_PATTERN.matcher(
                        text
                );


        if (emailMatcher.find()) {

            resume.setEmail(
                    emailMatcher.group()
            );
        }


        Matcher phoneMatcher =
                PHONE_PATTERN.matcher(
                        text
                );


        if (phoneMatcher.find()) {

            resume.setPhone(
                    phoneMatcher
                        .group()
                        .replaceAll(
                            "\\s+",
                            ""
                        )
            );
        }


        Set<String> links =
                new LinkedHashSet<>();


        Matcher urlMatcher =
                URL_PATTERN.matcher(
                        text
                );


        while (urlMatcher.find()) {

            links.add(
                    urlMatcher
                        .group()
                        .replaceAll(
                            "[,.;]+$",
                            ""
                        )
                        .trim()
            );
        }


        resume.setLinks(
                new ArrayList<>(
                        links
                )
        );


        resume.setLocation(
                extractHeaderLocation(
                        headerLines,
                        resume
                )
        );
    }


    private String extractHeaderLocation(
            List<String> lines,
            StructuredResume resume) {

        for (String line : lines) {

            String[] fragments =
                    line.split("\\|");


            for (String fragment :
                    fragments) {

                String value =
                        fragment;


                value =
                        removeLiteral(
                                value,
                                resume.getName()
                        );


                value =
                        removeLiteral(
                                value,
                                resume.getEmail()
                        );


                value =
                        removeLiteral(
                                value,
                                resume.getPhone()
                        );


                value =
                        URL_PATTERN
                            .matcher(value)
                            .replaceAll(" ");


                value =
                        value
                            .replaceAll(
                                "^[^A-Za-z]+",
                                ""
                            )
                            .replaceAll(
                                "[^A-Za-z ,.'-]",
                                " "
                            )
                            .replaceAll(
                                "\\s+",
                                " "
                            )
                            .trim();


                if (looksLikeLocation(
                        value
                )) {

                    return value;
                }
            }
        }


        return "";
    }


    private boolean looksLikeLocation(
            String value) {

        if (value == null
                ||
            value.isBlank()) {

            return false;
        }


        if (value.length() < 3
                ||
            value.length() > 45) {

            return false;
        }


        if (value.contains("@")
                ||
            value.contains("/")) {

            return false;
        }


        String lower =
                value.toLowerCase();


        if (lower.contains("resume")
                ||
            lower.contains("linkedin")
                ||
            lower.contains("github")
                ||
            lower.contains("leetcode")
                ||
            lower.contains("hackerrank")) {

            return false;
        }


        return value.matches(
                "[A-Za-z ,.'-]+"
        );
    }


    // =========================================================
    // EDUCATION
    // =========================================================

    private List<EducationEntry> parseEducation(
            String section) {

        List<EducationEntry> result =
                new ArrayList<>();


        for (String block :
                combineBulletBlocks(
                        section
                )) {

            String text =
                    stripBullet(
                            block
                    );


            if (text.isBlank()) {
                continue;
            }


            EducationEntry entry =
                    new EducationEntry();


            // ==========================================
            // DURATION
            // ==========================================

            Matcher durationMatcher =
                    YEAR_RANGE_PATTERN.matcher(
                            text
                    );


            if (durationMatcher.find()) {

                entry.setDuration(
                        durationMatcher
                            .group()
                            .trim()
                );


                text =
                        removeRange(
                                text,
                                durationMatcher.start(),
                                durationMatcher.end()
                        );
            }


            // ==========================================
            // INSTITUTION + QUALIFICATION
            // ==========================================

            Matcher qualificationMatcher =
                    QUALIFICATION_PATTERN.matcher(
                            text
                    );


            String institution =
                    text;


            String qualification =
                    "";


            if (qualificationMatcher.find()) {

                institution =
                        text.substring(
                                0,
                                qualificationMatcher.start()
                        );


                qualification =
                        text.substring(
                                qualificationMatcher.start()
                        );
            }


            institution =
                    cleanupSeparators(
                            institution
                    );


            qualification =
                    cleanupSeparators(
                            qualification
                    );


            // ==========================================
            // SCORE
            // ==========================================

            Matcher scoreMatcher =
                    SCORE_PATTERN.matcher(
                            qualification
                    );


            if (scoreMatcher.find()) {

                entry.setScore(
                        scoreMatcher
                            .group()
                            .trim()
                );


                qualification =
                        removeRange(
                                qualification,
                                scoreMatcher.start(),
                                scoreMatcher.end()
                        );
            }


            // ==========================================
            // LOCATION
            // ==========================================

            Matcher locationMatcher =
                    LOCATION_AT_END.matcher(
                            qualification
                    );


            if (locationMatcher.find()) {

                entry.setLocation(
                        locationMatcher
                            .group(1)
                            .trim()
                );


                qualification =
                        qualification
                            .substring(
                                0,
                                locationMatcher.start()
                            );
            }


            qualification =
                    cleanupSeparators(
                            qualification
                    );


            entry.setInstitution(
                    institution
            );


            entry.setQualification(
                    qualification
            );


            result.add(
                    entry
            );
        }


        return result;
    }


    // =========================================================
    // EXPERIENCE
    // =========================================================

    private List<ExperienceEntry> parseExperience(
            String section) {

        List<ExperienceEntry> result =
                new ArrayList<>();


        if (section == null
                ||
            section.isBlank()) {

            return result;
        }


        ExperienceEntry current =
                null;


        for (String raw :
                section.split("\\R")) {

            String line =
                    cleanup(raw);


            if (line.isBlank()) {
                continue;
            }


            String clean =
                    stripBullet(
                            line
                    );


            if (looksLikeExperienceHeader(
                    clean
            )) {

                if (current != null) {

                    result.add(
                            current
                    );
                }


                current =
                        parseExperienceHeader(
                                clean
                        );


                continue;
            }


            if (current == null) {

                current =
                        new ExperienceEntry();
            }


            current.getBullets()
                    .add(
                            clean
                    );
        }


        if (current != null) {

            result.add(
                    current
            );
        }


        return result;
    }


    private boolean looksLikeExperienceHeader(
            String value) {

        String lower =
                value.toLowerCase();


        boolean roleWord =
                lower.contains("intern")
                ||
                lower.contains("engineer")
                ||
                lower.contains("developer")
                ||
                lower.contains("analyst")
                ||
                lower.contains("associate")
                ||
                lower.contains("consultant");


        boolean duration =
                YEAR_RANGE_PATTERN
                        .matcher(
                                value
                        )
                        .find();


        return roleWord
                &&
                duration;
    }


    private ExperienceEntry parseExperienceHeader(
            String value) {

        ExperienceEntry entry =
                new ExperienceEntry();


        String working =
                value;


        Matcher duration =
                YEAR_RANGE_PATTERN.matcher(
                        working
                );


        if (duration.find()) {

            entry.setDuration(
                    duration
                        .group()
                        .trim()
            );


            working =
                    removeRange(
                            working,
                            duration.start(),
                            duration.end()
                    );
        }


        working =
                cleanupSeparators(
                        working
                );


        String[] parts =
                working.split(
                        "\\s+[–—-]\\s+",
                        2
                );


        if (parts.length == 2) {

            entry.setRole(
                    cleanupSeparators(
                            parts[0]
                    )
            );


            entry.setCompany(
                    cleanupSeparators(
                            parts[1]
                    )
            );

        } else {

            entry.setRole(
                    working
            );
        }


        return entry;
    }


    // =========================================================
    // SKILLS
    // =========================================================

    private List<SkillCategory> parseSkills(
            String section) {

        List<SkillCategory> result =
                new ArrayList<>();


        if (section == null
                ||
            section.isBlank()) {

            return result;
        }


        SkillCategory current =
                null;


        for (String raw :
                section.split("\\R")) {

            String line =
                    stripBullet(
                            cleanup(raw)
                    );


            if (line.isBlank()) {
                continue;
            }


            int colon =
                    line.indexOf(":");


            if (colon >= 0) {

                String categoryName =
                        line.substring(
                                0,
                                colon
                        ).trim();


                String skillsText =
                        line.substring(
                                colon + 1
                        ).trim();


                current =
                        new SkillCategory();


                current.setName(
                        categoryName
                );


                current.setSkills(
                        new ArrayList<>()
                );


                addCommaSeparatedSkills(
                        current,
                        skillsText
                );


                result.add(
                        current
                );


            } else if (current != null) {

                /*
                 * PDF extraction can wrap:
                 *
                 * Core Concepts: DSA, OOP, DBMS, SDLC,
                 * Software Testing, Database Design
                 *
                 * Previous parser lost the second line.
                 */
                addCommaSeparatedSkills(
                        current,
                        line
                );
            }
        }


        return result;
    }


    private void addCommaSeparatedSkills(
            SkillCategory category,
            String text) {

        if (category == null
                ||
            text == null
                ||
            text.isBlank()) {

            return;
        }


        if (category.getSkills()
                == null) {

            category.setSkills(
                    new ArrayList<>()
            );
        }


        for (String raw :
                text.split(",")) {

            String skill =
                    cleanupSeparators(
                            raw
                    );


            if (skill.isBlank()) {
                continue;
            }


            if (!containsIgnoreCase(
                    category.getSkills(),
                    skill
            )) {

                category.getSkills()
                        .add(
                                skill
                        );
            }
        }
    }


    // =========================================================
    // PROJECTS
    // =========================================================

    private List<ProjectEntry> parseProjects(
            String section) {

        List<ProjectEntry> result =
                new ArrayList<>();


        for (String block :
                combineBulletBlocks(
                        section
                )) {

            String value =
                    stripBullet(
                            block
                    );


            if (value.isBlank()) {
                continue;
            }


            ProjectEntry project =
                    new ProjectEntry();


            int separator =
                    findDescriptionSeparator(
                            value
                    );


            if (separator >= 0) {

                project.setName(
                        cleanupSeparators(
                                value.substring(
                                        0,
                                        separator
                                )
                        )
                );


                String description =
                        cleanupSeparators(
                                value.substring(
                                        separator + 1
                                )
                        );


                if (!description.isBlank()) {

                    project.getBullets()
                            .add(
                                    description
                            );
                }


            } else {

                project.setName(
                        cleanupSeparators(
                                value
                        )
                );
            }


            result.add(
                    project
            );
        }


        return result;
    }


    // =========================================================
    // SIMPLE SECTIONS
    // =========================================================

    private List<String> parseSimpleBullets(
            String section) {

        List<String> result =
                new ArrayList<>();


        for (String block :
                combineBulletBlocks(
                        section
                )) {

            String value =
                    stripBullet(
                            block
                    );


            if (!value.isBlank()) {

                result.add(
                        value
                );
            }
        }


        return result;
    }


    // =========================================================
    // SECTION EXTRACTION
    // =========================================================

    private String extractSection(
            String text,
            String... requestedHeadings) {

        Set<String> requested =
                new LinkedHashSet<>(
                        Arrays.asList(
                                requestedHeadings
                        )
                );


        StringBuilder output =
                new StringBuilder();


        boolean inside =
                false;


        for (String raw :
                text.split("\\R")) {

            String line =
                    cleanup(raw);


            String heading =
                    normalizeHeading(
                            line
                    );


            if (!inside) {

                if (requested.contains(
                        heading
                )) {

                    inside =
                            true;
                }


                continue;
            }


            if (HEADINGS.contains(
                    heading
            )) {

                break;
            }


            if (!line.isBlank()) {

                output.append(
                        line
                );

                output.append("\n");
            }
        }


        return output
                .toString()
                .trim();
    }


    // =========================================================
    // BULLET BLOCKS
    // =========================================================

    private List<String> combineBulletBlocks(
            String section) {

        List<String> result =
                new ArrayList<>();


        if (section == null
                ||
            section.isBlank()) {

            return result;
        }


        StringBuilder current =
                null;


        for (String raw :
                section.split("\\R")) {

            String line =
                    cleanup(raw);


            if (line.isBlank()) {
                continue;
            }


            if (isBullet(line)) {

                if (current != null) {

                    result.add(
                            current
                                .toString()
                                .trim()
                    );
                }


                current =
                        new StringBuilder(
                                line
                        );


            } else {

                if (current == null) {

                    current =
                            new StringBuilder(
                                    line
                            );

                } else {

                    current.append(" ");

                    current.append(
                            line
                    );
                }
            }
        }


        if (current != null) {

            result.add(
                    current
                        .toString()
                        .trim()
            );
        }


        return result;
    }


    // =========================================================
    // HELPERS
    // =========================================================

    private String normalizeText(
            String value) {

        return value
                .replace('\u00A0', ' ')
                .replaceAll(
                        "\\s+W\\s*(?=\\R|$)",
                        ""
                )
                .replace(
                        "\r\n",
                        "\n"
                )
                .replace(
                        "\r",
                        "\n"
                );
    }


    private String cleanup(
            String value) {

        if (value == null) {

            return "";
        }


        return value
                .replace('\u00A0', ' ')
                .replaceAll(
                        "\\s+W\\s*$",
                        ""
                )
                .replaceAll(
                        "[\\t]+",
                        " "
                )
                .replaceAll(
                        " {2,}",
                        " "
                )
                .trim();
    }


    private String cleanupSeparators(
            String value) {

        return cleanup(value)
                .replaceAll(
                        "^[|,:;\\-–—\\s]+",
                        ""
                )
                .replaceAll(
                        "[|,:;\\-–—\\s]+$",
                        ""
                )
                .replaceAll(
                        "\\s{2,}",
                        " "
                )
                .trim();
    }


    private String cleanHeaderName(
            String value) {

        return cleanup(value)
                .replaceAll(
                        "^[^A-Za-z]+",
                        ""
                )
                .trim();
    }


    private String paragraph(
            String value) {

        return cleanup(value)
                .replaceAll(
                        "\\s*\\R\\s*",
                        " "
                )
                .replaceAll(
                        "\\s{2,}",
                        " "
                )
                .trim();
    }


    private boolean isHeading(
            String value) {

        return HEADINGS.contains(
                normalizeHeading(
                        value
                )
        );
    }


    private String normalizeHeading(
            String value) {

        return cleanup(value)
                .toLowerCase()
                .replace(":", "")
                .trim();
    }


    private boolean isBullet(
            String value) {

        return value != null
                &&
                value.matches(
                        "^[•●▪◦*-]\\s*.+"
                );
    }


    private String stripBullet(
            String value) {

        return cleanup(value)
                .replaceFirst(
                        "^[•●▪◦*-]\\s*",
                        ""
                )
                .trim();
    }


    private int findDescriptionSeparator(
            String value) {

        int index =
                value.indexOf(
                        " – "
                );


        if (index >= 0) {

            return index + 1;
        }


        index =
                value.indexOf(
                        " — "
                );


        if (index >= 0) {

            return index + 1;
        }


        index =
                value.indexOf(
                        " - "
                );


        if (index >= 0) {

            return index + 1;
        }


        return -1;
    }


    private String removeRange(
            String value,
            int start,
            int end) {

        return cleanup(
                value.substring(
                        0,
                        start
                )
                + " "
                + value.substring(
                        end
                )
        );
    }


    private String removeLiteral(
            String source,
            String target) {

        if (source == null
                ||
            target == null
                ||
            target.isBlank()) {

            return source;
        }


        return source.replace(
                target,
                " "
        );
    }


    private boolean containsIgnoreCase(
            List<String> values,
            String target) {

        for (String value :
                values) {

            if (value.equalsIgnoreCase(
                    target
            )) {

                return true;
            }
        }


        return false;
    }
}