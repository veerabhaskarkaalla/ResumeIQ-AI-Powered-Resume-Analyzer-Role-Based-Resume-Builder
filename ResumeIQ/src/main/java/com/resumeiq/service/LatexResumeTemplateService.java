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
public class LatexResumeTemplateService {

    public String render(
            StructuredResume resume) {

        validate(resume);

        LayoutProfile profile =
                chooseLayout(
                        resume
                );

        StringBuilder tex =
                new StringBuilder();

        appendPreamble(
                tex,
                profile
        );

        tex.append(
                "\\begin{document}\n"
        );

        tex.append(
                "\\fontsize{"
                + profile.bodyFont
                + "}{"
                + profile.lineHeight
                + "}\\selectfont\n"
        );

        tex.append(
                "\\raggedright\n"
        );

        /*
         * Important:
         *
         * Avoid a large empty area at the bottom.
         * LaTeX can distribute small amounts of
         * vertical flexibility through the document.
         */
        tex.append(
                "\\flushbottom\n\n"
        );


        appendHeader(
                tex,
                resume,
                profile
        );

        appendCareerObjective(
                tex,
                resume
        );

        appendEducation(
                tex,
                resume
        );

        appendExperience(
                tex,
                resume
        );

        appendSkills(
                tex,
                resume
        );

        appendProjects(
                tex,
                resume
        );

        appendSimpleSection(
                tex,
                "Research Publication",
                resume.getResearchPublications()
        );

        appendSimpleSection(
                tex,
                "Certifications",
                resume.getCertifications()
        );

        appendSimpleSection(
                tex,
                "Achievements",
                resume.getAchievements()
        );


        tex.append(
                "\n\\end{document}\n"
        );


        return tex.toString();
    }


    // =========================================================
    // ADAPTIVE LAYOUT
    // =========================================================

    private LayoutProfile chooseLayout(
            StructuredResume resume) {

        int density =
                estimateDensity(
                        resume
                );


        /*
         * Low content:
         *
         * Increase font and spacing so that
         * half of the page does not remain empty.
         */
        if (density <= 42) {

            return LayoutProfile.spacious();
        }


        /*
         * Normal resume.
         */
        if (density <= 57) {

            return LayoutProfile.normal();
        }


        /*
         * Heavy resume:
         *
         * Compact spacing before reducing font
         * aggressively.
         */
        return LayoutProfile.compact();
    }


    private int estimateDensity(
            StructuredResume resume) {

        int units = 0;


        // =====================================================
        // HEADER
        // =====================================================

        units += 3;


        // =====================================================
        // OBJECTIVE
        // =====================================================

        String objective =
                safe(
                    resume.getCareerObjective()
                );


        if (!objective.isBlank()) {

            units += 1;

            units += wrappedUnits(
                    objective,
                    100
            );
        }


        // =====================================================
        // EDUCATION
        // =====================================================

        if (resume.getEducation() != null
                &&
            !resume.getEducation().isEmpty()) {

            units += 1;


            for (EducationEntry entry :
                    resume.getEducation()) {

                if (entry == null) {

                    continue;
                }


                units += 2;


                if (
                    safe(
                        entry.getLocation()
                    ).length() > 35
                ) {

                    units += 1;
                }
            }
        }


        // =====================================================
        // EXPERIENCE
        // =====================================================

        if (resume.getExperience() != null
                &&
            !resume.getExperience().isEmpty()) {

            units += 1;


            for (ExperienceEntry entry :
                    resume.getExperience()) {

                if (entry == null) {

                    continue;
                }


                units += 2;


                if (entry.getBullets() != null) {

                    for (String bullet :
                            entry.getBullets()) {

                        units +=
                                wrappedUnits(
                                        bullet,
                                        105
                                );
                    }
                }
            }
        }


        // =====================================================
        // SKILLS
        // =====================================================

        if (resume.getSkillCategories() != null
                &&
            !resume.getSkillCategories().isEmpty()) {

            units += 1;


            for (SkillCategory category :
                    resume.getSkillCategories()) {

                if (category == null
                        ||
                    category.getSkills() == null) {

                    continue;
                }


                String text =
                        safe(
                            category.getName()
                        )
                        + ": "
                        + String.join(
                            ", ",
                            category.getSkills()
                        );


                units +=
                        wrappedUnits(
                                text,
                                110
                        );
            }
        }


        // =====================================================
        // PROJECTS
        // =====================================================

        if (resume.getProjects() != null
                &&
            !resume.getProjects().isEmpty()) {

            units += 1;


            for (ProjectEntry project :
                    resume.getProjects()) {

                if (project == null) {

                    continue;
                }


                units += 1;


                if (project.getBullets() != null) {

                    for (String bullet :
                            project.getBullets()) {

                        units +=
                                wrappedUnits(
                                        bullet,
                                        105
                                );
                    }
                }
            }
        }


        // =====================================================
        // OTHER SECTIONS
        // =====================================================

        units +=
                estimateListSection(
                        resume.getResearchPublications()
                );


        units +=
                estimateListSection(
                        resume.getCertifications()
                );


        units +=
                estimateListSection(
                        resume.getAchievements()
                );


        return units;
    }


    private int estimateListSection(
            List<String> values) {

        if (values == null
                ||
            values.isEmpty()) {

            return 0;
        }


        int units = 1;


        for (String value : values) {

            units +=
                    wrappedUnits(
                            value,
                            105
                    );
        }


        return units;
    }


    private int wrappedUnits(
            String value,
            int charsPerLine) {

        String text =
                normalize(
                        value
                );


        if (text.isBlank()) {

            return 0;
        }


        return Math.max(
                1,
                (int) Math.ceil(
                        text.length()
                        /
                        (double) charsPerLine
                )
        );
    }


    // =========================================================
    // PREAMBLE
    // =========================================================

    private void appendPreamble(
            StringBuilder tex,
            LayoutProfile profile) {

        tex.append(
                "\\documentclass[10pt,a4paper]{article}\n\n"
        );


        tex.append(
                "\\usepackage["
        );

        tex.append(
                "top="
                + profile.topMargin
                + "in,"
        );

        tex.append(
                "bottom="
                + profile.bottomMargin
                + "in,"
        );

        tex.append(
                "left="
                + profile.sideMargin
                + "in,"
        );

        tex.append(
                "right="
                + profile.sideMargin
                + "in"
        );

        tex.append(
                "]{geometry}\n\n"
        );


        tex.append("""
                \\usepackage[T1]{fontenc}
                \\usepackage[utf8]{inputenc}
                \\usepackage{lmodern}
                \\usepackage{enumitem}
                \\usepackage{titlesec}
                \\usepackage[hidelinks]{hyperref}
                \\usepackage{microtype}

                \\IfFileExists{glyphtounicode.tex}{
                    \\input{glyphtounicode}
                    \\pdfgentounicode=1
                }{}

                \\pagestyle{empty}

                \\setlength{\\parindent}{0pt}

                \\setlength{\\emergencystretch}{2em}

                \\hyphenpenalty=10000
                \\exhyphenpenalty=10000

                \\urlstyle{same}

                \\renewcommand{\\familydefault}{\\sfdefault}

                """);


        /*
         * Flexible paragraph spacing.
         *
         * "plus" allows LaTeX to distribute a
         * small amount of space if the page is sparse.
         */
        tex.append(
                "\\setlength{\\parskip}{"
                + profile.paragraphGap
                + "pt plus "
                + profile.paragraphStretch
                + "pt}\n\n"
        );


        // =====================================================
        // SECTION STYLE
        // =====================================================

        tex.append(
                "\\titleformat{\\section}\n"
        );

        tex.append(
                "  {\\bfseries\\fontsize{"
                + profile.headingFont
                + "}{"
                + profile.headingLineHeight
                + "}\\selectfont}\n"
        );

        tex.append(
                "  {}\n"
        );

        tex.append(
                "  {0pt}\n"
        );

        tex.append(
                "  {\\MakeUppercase}\n"
        );

        tex.append(
                "  [\\vspace{-"
                + profile.ruleOffset
                + "pt}\\rule{\\textwidth}{0.45pt}]\n\n"
        );


        tex.append(
                "\\titlespacing*{\\section}\n"
        );

        tex.append(
                "  {0pt}\n"
        );

        tex.append(
                "  {"
                + profile.sectionBefore
                + "pt plus "
                + profile.sectionStretch
                + "pt}\n"
        );

        tex.append(
                "  {"
                + profile.sectionAfter
                + "pt}\n\n"
        );


        // =====================================================
        // BULLETS
        // =====================================================

        tex.append(
                "\\setlist[itemize]{\n"
        );

        tex.append(
                "    leftmargin=13pt,\n"
        );

        tex.append(
                "    labelsep=4pt,\n"
        );

        tex.append(
                "    itemsep="
                + profile.itemGap
                + "pt plus "
                + profile.itemStretch
                + "pt,\n"
        );

        tex.append(
                "    topsep="
                + profile.listTopGap
                + "pt,\n"
        );

        tex.append(
                "    parsep=0pt,\n"
        );

        tex.append(
                "    partopsep=0pt\n"
        );

        tex.append(
                "}\n\n"
        );


        // =====================================================
        // EDUCATION
        // =====================================================

        tex.append("""
                \\newcommand{\\resumeEntry}[4]{%
                    \\noindent
                    \\textbf{#1}%
                    \\hfill
                    \\textbf{#2}%
                    \\par

                    \\noindent
                    #3%

                    \\if\\relax\\detokenize{#4}\\relax
                    \\else
                        \\hfill
                        \\textit{#4}%
                    \\fi

                    \\par
                }

                """);


        // =====================================================
        // EXPERIENCE
        // =====================================================

        tex.append("""
                \\newcommand{\\experienceEntry}[4]{%
                    \\noindent
                    \\textbf{#1}%
                    \\hfill
                    \\textbf{#2}%
                    \\par

                    \\if\\relax\\detokenize{#3#4}\\relax
                    \\else
                        \\noindent
                        \\textit{#3}%

                        \\if\\relax\\detokenize{#4}\\relax
                        \\else
                            \\hfill
                            \\textit{#4}%
                        \\fi

                        \\par
                    \\fi
                }

                """);


        // =====================================================
        // PROJECT
        // =====================================================

        tex.append("""
                \\newcommand{\\projectEntry}[2]{%
                    \\noindent
                    \\textbf{#1}%

                    \\if\\relax\\detokenize{#2}\\relax
                    \\else
                        \\hfill
                        \\textit{#2}%
                    \\fi

                    \\par
                }

                """);


        // =====================================================
        // SKILLS
        // =====================================================

        tex.append("""
                \\newcommand{\\skillRow}[2]{%
                    \\noindent
                    \\textbf{#1:} #2%
                    \\par
                }

                """);
    }


    // =========================================================
    // HEADER
    // =========================================================

    private void appendHeader(
            StringBuilder tex,
            StructuredResume resume,
            LayoutProfile profile) {

        String name =
                normalize(
                        resume.getName()
                );


        tex.append(
                "{\\fontsize{"
                + profile.nameFont
                + "}{"
                + profile.nameLineHeight
                + "}\\selectfont"
        );


        tex.append(
                "\\textbf{"
        );


        tex.append(
                escape(
                        name.toUpperCase()
                )
        );


        tex.append(
                "}}\\par\n"
        );


        tex.append(
                "\\vspace{"
                + profile.nameAfter
                + "pt}\n"
        );


        List<String> contacts =
                new ArrayList<>();


        add(
                contacts,
                resume.getEmail()
        );


        add(
                contacts,
                resume.getPhone()
        );


        add(
                contacts,
                resume.getLocation()
        );


        if (!contacts.isEmpty()) {

            tex.append(
                    "{\\fontsize{"
                    + profile.contactFont
                    + "}{"
                    + profile.contactLineHeight
                    + "}\\selectfont "
            );


            tex.append(
                    joinLatex(
                            contacts,
                            " \\enspace $|$ \\enspace "
                    )
            );


            tex.append(
                    "}\\par\n"
            );
        }


        List<String> links =
                cleanList(
                        resume.getLinks()
                );


        if (!links.isEmpty()) {

            List<String> rendered =
                    new ArrayList<>();


            int index = 1;


            for (String link : links) {

                String value =
                        renderLink(
                                link,
                                index
                        );


                if (!value.isBlank()) {

                    rendered.add(
                            value
                    );
                }


                index++;
            }


            if (!rendered.isEmpty()) {

                tex.append(
                        "{\\fontsize{"
                        + profile.linkFont
                        + "}{"
                        + profile.linkLineHeight
                        + "}\\selectfont "
                );


                tex.append(
                        String.join(
                                " \\enspace $|$ \\enspace ",
                                rendered
                        )
                );


                tex.append(
                        "}\\par\n"
                );
            }
        }


        tex.append(
                "\\vspace{"
                + profile.headerRuleBefore
                + "pt}\n"
        );


        tex.append(
                "\\noindent\\rule{\\textwidth}{0.6pt}\\par\n"
        );


        tex.append(
                "\\vspace{"
                + profile.headerAfter
                + "pt}\n"
        );
    }


    // =========================================================
    // CAREER OBJECTIVE
    // =========================================================

    private void appendCareerObjective(
            StringBuilder tex,
            StructuredResume resume) {

        String objective =
                normalize(
                        resume.getCareerObjective()
                );


        if (objective.isBlank()) {

            return;
        }


        appendSection(
                tex,
                "Career Objective"
        );


        tex.append(
                "\\noindent "
        );


        tex.append(
                escape(
                        objective
                )
        );


        tex.append(
                "\\par\n"
        );
    }


    // =========================================================
    // EDUCATION
    // =========================================================

    private void appendEducation(
            StringBuilder tex,
            StructuredResume resume) {

        if (resume.getEducation() == null
                ||
            resume.getEducation().isEmpty()) {

            return;
        }


        appendSection(
                tex,
                "Education"
        );


        for (EducationEntry entry :
                resume.getEducation()) {

            if (entry == null) {

                continue;
            }


            String institution =
                    normalize(
                            entry.getInstitution()
                    );


            String duration =
                    normalize(
                            entry.getDuration()
                    );


            String qualification =
                    normalize(
                            entry.getQualification()
                    );


            String score =
                    normalize(
                            entry.getScore()
                    );


            String location =
                    normalize(
                            entry.getLocation()
                    );


            if (institution.isBlank()
                    &&
                qualification.isBlank()) {

                continue;
            }


            tex.append(
                    "\\resumeEntry"
            );


            argument(
                    tex,
                    institution
            );


            argument(
                    tex,
                    duration
            );


            argument(
                    tex,
                    joinNonBlank(
                            qualification,
                            score
                    )
            );


            argument(
                    tex,
                    location
            );


            tex.append("\n");
        }
    }


    // =========================================================
    // EXPERIENCE
    // =========================================================

    private void appendExperience(
            StringBuilder tex,
            StructuredResume resume) {

        if (resume.getExperience() == null
                ||
            resume.getExperience().isEmpty()) {

            return;
        }


        appendSection(
                tex,
                "Work Experience"
        );


        for (ExperienceEntry entry :
                resume.getExperience()) {

            if (entry == null) {

                continue;
            }


            String role =
                    normalize(
                            entry.getRole()
                    );


            String company =
                    normalize(
                            entry.getCompany()
                    );


            String duration =
                    normalize(
                            entry.getDuration()
                    );


            if (role.isBlank()
                    &&
                company.isBlank()) {

                continue;
            }


            tex.append(
                    "\\experienceEntry"
            );


            argument(
                    tex,
                    role
            );


            argument(
                    tex,
                    duration
            );


            argument(
                    tex,
                    company
            );


            argument(
                    tex,
                    ""
            );


            tex.append("\n");


            appendBullets(
                    tex,
                    entry.getBullets()
            );
        }
    }


    // =========================================================
    // SKILLS
    // =========================================================

    private void appendSkills(
            StringBuilder tex,
            StructuredResume resume) {

        if (resume.getSkillCategories() == null
                ||
            resume.getSkillCategories().isEmpty()) {

            return;
        }


        appendSection(
                tex,
                "Technical Skills"
        );


        for (SkillCategory category :
                resume.getSkillCategories()) {

            if (category == null) {

                continue;
            }


            String name =
                    normalize(
                            category.getName()
                    );


            List<String> skills =
                    cleanList(
                            category.getSkills()
                    );


            if (name.isBlank()
                    ||
                skills.isEmpty()) {

                continue;
            }


            tex.append(
                    "\\skillRow"
            );


            argument(
                    tex,
                    name
            );


            argument(
                    tex,
                    String.join(
                            ", ",
                            skills
                    )
            );


            tex.append("\n");
        }
    }


    // =========================================================
    // PROJECTS
    // =========================================================

    private void appendProjects(
            StringBuilder tex,
            StructuredResume resume) {

        if (resume.getProjects() == null
                ||
            resume.getProjects().isEmpty()) {

            return;
        }


        appendSection(
                tex,
                "Projects"
        );


        for (ProjectEntry project :
                resume.getProjects()) {

            if (project == null) {

                continue;
            }


            String name =
                    normalize(
                            project.getName()
                    );


            String technologies =
                    normalize(
                            project.getTechnologies()
                    );


            if (name.isBlank()) {

                continue;
            }


            tex.append(
                    "\\projectEntry"
            );


            argument(
                    tex,
                    name
            );


            argument(
                    tex,
                    technologies
            );


            tex.append("\n");


            appendBullets(
                    tex,
                    project.getBullets()
            );
        }
    }


    // =========================================================
    // SIMPLE LIST SECTIONS
    // =========================================================

    private void appendSimpleSection(
            StringBuilder tex,
            String title,
            List<String> values) {

        List<String> clean =
                cleanList(
                        values
                );


        if (clean.isEmpty()) {

            return;
        }


        appendSection(
                tex,
                title
        );


        appendBullets(
                tex,
                clean
        );
    }


    // =========================================================
    // BULLETS
    // =========================================================

    private void appendBullets(
            StringBuilder tex,
            List<String> bullets) {

        List<String> clean =
                cleanList(
                        bullets
                );


        if (clean.isEmpty()) {

            return;
        }


        tex.append(
                "\\begin{itemize}\n"
        );


        for (String bullet : clean) {

            String value =
                    cleanBullet(
                            bullet
                    );


            if (value.isBlank()) {

                continue;
            }


            tex.append(
                    "\\item "
            );


            tex.append(
                    escape(
                            value
                    )
            );


            tex.append("\n");
        }


        tex.append(
                "\\end{itemize}\n"
        );
    }


    // =========================================================
    // SECTION
    // =========================================================

    private void appendSection(
            StringBuilder tex,
            String title) {

        tex.append(
                "\\section{"
        );


        tex.append(
                escape(
                        title
                )
        );


        tex.append(
                "}\n"
        );


        tex.append(
                "\\nopagebreak[4]\n"
        );
    }


    // =========================================================
    // LINKS
    // =========================================================

    private String renderLink(
            String raw,
            int index) {

        String value =
                normalize(
                        raw
                );


        if (value.isBlank()) {

            return "";
        }


        String lower =
                value.toLowerCase();


        String label;


        if (lower.contains(
                "linkedin"
        )) {

            label = "LinkedIn";

        } else if (lower.contains(
                "github"
        )) {

            label = "GitHub";

        } else if (lower.contains(
                "leetcode"
        )) {

            label = "LeetCode";

        } else if (lower.contains(
                "hackerrank"
        )) {

            label = "HackerRank";

        } else if (lower.contains(
                "portfolio"
        )) {

            label = "Portfolio";

        } else {

            label =
                    "Profile " + index;
        }


        String url =
                normalizeUrl(
                        value
                );


        if (url.isBlank()) {

            return escape(
                    value
            );
        }


        return "\\href{\\detokenize{"
                + url
                + "}}{"
                + escape(label)
                + "}";
    }


    private String normalizeUrl(
            String value) {

        String text =
                normalize(
                        value
                );


        if (text.isBlank()) {

            return "";
        }


        int https =
                text.indexOf(
                        "https://"
                );


        if (https >= 0) {

            return trimUrl(
                    text.substring(
                            https
                    )
            );
        }


        int http =
                text.indexOf(
                        "http://"
                );


        if (http >= 0) {

            return trimUrl(
                    text.substring(
                            http
                    )
            );
        }


        String lower =
                text.toLowerCase();


        if (
                lower.contains(
                        "linkedin.com/"
                )
                ||
                lower.contains(
                        "github.com/"
                )
                ||
                lower.contains(
                        "leetcode.com/"
                )
                ||
                lower.contains(
                        "hackerrank.com/"
                )
                ||
                lower.startsWith(
                        "www."
                )
        ) {

            return trimUrl(
                    "https://" + text
            );
        }


        return "";
    }


    private String trimUrl(
            String value) {

        String url =
                safe(
                        value
                ).trim();


        int space =
                url.indexOf(' ');


        if (space > 0) {

            url =
                    url.substring(
                            0,
                            space
                    );
        }


        while (
                url.endsWith("|")
                ||
                url.endsWith(",")
                ||
                url.endsWith(";")
        ) {

            url =
                    url.substring(
                            0,
                            url.length() - 1
                    );
        }


        return url;
    }


    // =========================================================
    // LATEX ARGUMENT
    // =========================================================

    private void argument(
            StringBuilder tex,
            String value) {

        tex.append("{");

        tex.append(
                escape(
                        safe(
                                value
                        )
                )
        );

        tex.append("}");
    }


    // =========================================================
    // LATEX ESCAPE
    // =========================================================

    private String escape(
            String value) {

        String text =
                normalize(
                        value
                );


        StringBuilder result =
                new StringBuilder();


        for (char c :
                text.toCharArray()) {

            switch (c) {

                case '\\' ->
                    result.append(
                            "\\textbackslash{}"
                    );

                case '&' ->
                    result.append(
                            "\\&"
                    );

                case '%' ->
                    result.append(
                            "\\%"
                    );

                case '$' ->
                    result.append(
                            "\\$"
                    );

                case '#' ->
                    result.append(
                            "\\#"
                    );

                case '_' ->
                    result.append(
                            "\\_"
                    );

                case '{' ->
                    result.append(
                            "\\{"
                    );

                case '}' ->
                    result.append(
                            "\\}"
                    );

                case '~' ->
                    result.append(
                            "\\textasciitilde{}"
                    );

                case '^' ->
                    result.append(
                            "\\textasciicircum{}"
                    );

                default ->
                    result.append(c);
            }
        }


        return result.toString();
    }


    // =========================================================
    // CONTACT JOIN
    // =========================================================

    private String joinLatex(
            List<String> values,
            String separator) {

        List<String> escaped =
                new ArrayList<>();


        for (String value :
                values) {

            String clean =
                    normalize(
                            value
                    );


            if (!clean.isBlank()) {

                escaped.add(
                        escape(
                                clean
                        )
                );
            }
        }


        return String.join(
                separator,
                escaped
        );
    }


    // =========================================================
    // EDUCATION DETAILS
    // =========================================================

    private String joinNonBlank(
            String first,
            String second) {

        String a =
                normalize(
                        first
                );


        String b =
                normalize(
                        second
                );


        if (a.isBlank()) {

            return b;
        }


        if (b.isBlank()) {

            return a;
        }


        if (
                a.toLowerCase()
                        .contains(
                                b.toLowerCase()
                        )
        ) {

            return a;
        }


        return a
                + " | "
                + b;
    }


    // =========================================================
    // CLEAN LIST
    // =========================================================

    private List<String> cleanList(
            List<String> values) {

        List<String> result =
                new ArrayList<>();


        if (values == null) {

            return result;
        }


        for (String value :
                values) {

            String clean =
                    normalize(
                            value
                    );


            if (
                    !clean.isBlank()
                    &&
                    !result.contains(
                            clean
                    )
            ) {

                result.add(
                        clean
                );
            }
        }


        return result;
    }


    private void add(
            List<String> values,
            String value) {

        String clean =
                normalize(
                        value
                );


        if (!clean.isBlank()) {

            values.add(
                    clean
            );
        }
    }


    // =========================================================
    // BULLET CLEANING
    // =========================================================

    private String cleanBullet(
            String value) {

        return normalize(
                value
        )
                .replaceFirst(
                        "^[•●▪◦*-]\\s*",
                        ""
                )
                .trim();
    }


    // =========================================================
    // NORMALIZE
    // =========================================================

    private String normalize(
            String value) {

        return safe(
                value
        )

                .replace(
                        '\u00A0',
                        ' '
                )

                .replace(
                        '–',
                        '-'
                )

                .replace(
                        '—',
                        '-'
                )

                .replace(
                        '’',
                        '\''
                )

                .replace(
                        '‘',
                        '\''
                )

                .replace(
                        '“',
                        '"'
                )

                .replace(
                        '”',
                        '"'
                )

                .replaceAll(
                        "[\\r\\n\\t]+",
                        " "
                )

                .replaceAll(
                        " {2,}",
                        " "
                )

                .trim();
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }


    // =========================================================
    // VALIDATE
    // =========================================================

    private void validate(
            StructuredResume resume) {

        if (resume == null) {

            throw new IllegalArgumentException(
                    "Structured resume is required"
            );
        }


        if (
                safe(
                        resume.getName()
                ).isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Resume name is required"
            );
        }
    }


    // =========================================================
    // LAYOUT PROFILE
    // =========================================================

    private static class LayoutProfile {

        private double bodyFont;

        private double lineHeight;

        private double nameFont;

        private double nameLineHeight;

        private double contactFont;

        private double contactLineHeight;

        private double linkFont;

        private double linkLineHeight;

        private double headingFont;

        private double headingLineHeight;

        private double topMargin;

        private double bottomMargin;

        private double sideMargin;

        private double paragraphGap;

        private double paragraphStretch;

        private double sectionBefore;

        private double sectionAfter;

        private double sectionStretch;

        private double itemGap;

        private double itemStretch;

        private double listTopGap;

        private double ruleOffset;

        private double nameAfter;

        private double headerRuleBefore;

        private double headerAfter;


        // =====================================================
        // SPACIOUS
        // =====================================================

        private static LayoutProfile spacious() {

            LayoutProfile p =
                    new LayoutProfile();


            p.bodyFont = 10.0;

            p.lineHeight = 11.4;


            p.nameFont = 19.0;

            p.nameLineHeight = 20.0;


            p.contactFont = 9.2;

            p.contactLineHeight = 10.0;


            p.linkFont = 8.8;

            p.linkLineHeight = 9.5;


            p.headingFont = 10.7;

            p.headingLineHeight = 11.6;


            p.topMargin = 0.38;

            p.bottomMargin = 0.38;

            p.sideMargin = 0.50;


            p.paragraphGap = 0.6;

            p.paragraphStretch = 0.7;


            p.sectionBefore = 5.0;

            p.sectionAfter = 3.6;

            p.sectionStretch = 1.5;


            p.itemGap = 0.8;

            p.itemStretch = 0.5;


            p.listTopGap = 0.7;


            p.ruleOffset = 3.0;


            p.nameAfter = 2.5;

            p.headerRuleBefore = 2.5;

            p.headerAfter = 1.5;


            return p;
        }


        // =====================================================
        // NORMAL
        // =====================================================

        private static LayoutProfile normal() {

            LayoutProfile p =
                    new LayoutProfile();


            p.bodyFont = 9.4;

            p.lineHeight = 10.4;


            p.nameFont = 17.8;

            p.nameLineHeight = 18.8;


            p.contactFont = 8.8;

            p.contactLineHeight = 9.4;


            p.linkFont = 8.4;

            p.linkLineHeight = 9.0;


            p.headingFont = 10.1;

            p.headingLineHeight = 10.8;


            p.topMargin = 0.31;

            p.bottomMargin = 0.31;

            p.sideMargin = 0.44;


            p.paragraphGap = 0.2;

            p.paragraphStretch = 0.4;


            p.sectionBefore = 3.4;

            p.sectionAfter = 2.4;

            p.sectionStretch = 0.8;


            p.itemGap = 0.2;

            p.itemStretch = 0.2;


            p.listTopGap = 0.2;


            p.ruleOffset = 3.0;


            p.nameAfter = 1.2;

            p.headerRuleBefore = 0.8;

            p.headerAfter = -0.5;


            return p;
        }


        // =====================================================
        // COMPACT
        // =====================================================

        private static LayoutProfile compact() {

            LayoutProfile p =
                    new LayoutProfile();


            p.bodyFont = 8.9;

            p.lineHeight = 9.65;


            p.nameFont = 17.0;

            p.nameLineHeight = 17.8;


            p.contactFont = 8.3;

            p.contactLineHeight = 8.8;


            p.linkFont = 8.0;

            p.linkLineHeight = 8.5;


            p.headingFont = 9.6;

            p.headingLineHeight = 10.1;


            p.topMargin = 0.25;

            p.bottomMargin = 0.25;

            p.sideMargin = 0.40;


            p.paragraphGap = 0.0;

            p.paragraphStretch = 0.1;


            p.sectionBefore = 2.2;

            p.sectionAfter = 1.5;

            p.sectionStretch = 0.2;


            p.itemGap = 0.0;

            p.itemStretch = 0.0;


            p.listTopGap = 0.0;


            p.ruleOffset = 2.7;


            p.nameAfter = 0.4;

            p.headerRuleBefore = 0.0;

            p.headerAfter = -1.0;


            return p;
        }
    }
}