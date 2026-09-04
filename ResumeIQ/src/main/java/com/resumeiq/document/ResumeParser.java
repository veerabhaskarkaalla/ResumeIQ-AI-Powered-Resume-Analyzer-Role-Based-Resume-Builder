package com.resumeiq.document;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.resumeiq.dto.ParsedResume;
import com.resumeiq.util.SkillCatalog;

@Component
public class ResumeParser {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
            );

    private static final Pattern PHONE_PATTERN =
            Pattern.compile(
                    "(?:\\+91[-\\s]?)?[6-9]\\d{9}"
            );

    public ParsedResume parse(String text) {

        ParsedResume parsedResume =
                new ParsedResume();

        if (text == null) {
            text = "";
        }

        parsedResume.setName(
                extractName(text)
        );

        parsedResume.setEmail(
                extractEmail(text)
        );

        parsedResume.setPhone(
                extractPhone(text)
        );

        parsedResume.setSkills(
                SkillCatalog.findSkills(text)
        );

        parsedResume.setSummary(
                extractSection(
                        text,
                        "professional summary",
                        "summary",
                        "career objective",
                        "objective"
                )
        );

        parsedResume.setEducation(
                extractSection(
                        text,
                        "education",
                        "academic qualifications",
                        "academic qualification",
                        "academics"
                )
        );

        parsedResume.setExperience(
                extractSection(
                        text,
                        "experience",
                        "work experience",
                        "professional experience",
                        "internship",
                        "internships"
                )
        );

        parsedResume.setProjects(
                extractSection(
                        text,
                        "projects",
                        "academic projects",
                        "personal projects",
                        "project experience"
                )
        );

        parsedResume.setCertifications(
                extractSection(
                        text,
                        "certifications",
                        "certification",
                        "certificates",
                        "licenses & certifications"
                )
        );

        return parsedResume;
    }

    private String extractName(String text) {

        String[] lines = text.split("\\R");

        for (String line : lines) {

            String cleaned =
                    line.trim()
                        .replace("|", "")
                        .trim();

            if (!cleaned.isEmpty()
                    && !cleaned.contains("@")
                    && !cleaned.matches(".*\\d.*")
                    && cleaned.length() >= 3
                    && cleaned.length() <= 60
                    && !isAnotherMainHeading(cleaned)) {

                return cleaned;
            }
        }

        return "";
    }

    private String extractEmail(String text) {

        Matcher matcher =
                EMAIL_PATTERN.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return "";
    }

    private String extractPhone(String text) {

        String cleaned =
                text.replaceAll("[()]", "")
                    .replaceAll("-", "")
                    .replaceAll("\\s+", " ");

        Matcher matcher =
                PHONE_PATTERN.matcher(cleaned);

        if (matcher.find()) {
            return matcher.group();
        }

        return "";
    }

    private String extractSection(
            String text,
            String... headings) {

        String[] lines = text.split("\\R");

        boolean insideSection = false;

        StringBuilder result =
                new StringBuilder();

        for (String line : lines) {

            String trimmed =
                    cleanHeading(line);

            if (isHeading(trimmed, headings)) {

                insideSection = true;
                continue;
            }

            if (insideSection
                    && isAnotherMainHeading(trimmed)) {

                break;
            }

            if (insideSection
                    && !line.trim().isEmpty()) {

                result.append(line.trim())
                      .append("\n");
            }
        }

        return result.toString().trim();
    }

    private String cleanHeading(String line) {

        if (line == null) {
            return "";
        }

        return line
                .trim()
                .replace(":", "")
                .replace("-", "")
                .trim();
    }

    private boolean isHeading(
            String line,
            String... headings) {

        for (String heading : headings) {

            if (line.equalsIgnoreCase(heading)) {
                return true;
            }
        }

        return false;
    }

    private boolean isAnotherMainHeading(
            String line) {

        List<String> headings =
                Arrays.asList(
                        "professional summary",
                        "summary",
                        "career objective",
                        "objective",

                        "skills",
                        "technical skills",
                        "core skills",

                        "education",
                        "academic qualifications",
                        "academic qualification",
                        "academics",

                        "experience",
                        "work experience",
                        "professional experience",

                        "internship",
                        "internships",

                        "projects",
                        "academic projects",
                        "personal projects",
                        "project experience",

                        "certifications",
                        "certification",
                        "certificates",
                        "licenses & certifications",

                        "achievements",
                        "awards",

                        "publications",
                        "research",

                        "languages",
                        "interests"
                );

        for (String heading : headings) {

            if (line.equalsIgnoreCase(heading)) {
                return true;
            }
        }

        return false;
    }
}