package com.resumeiq.service;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import org.springframework.stereotype.Service;

import com.resumeiq.dto.EducationEntry;
import com.resumeiq.dto.ExperienceEntry;
import com.resumeiq.dto.ProjectEntry;
import com.resumeiq.dto.SkillCategory;
import com.resumeiq.dto.StructuredResume;

@Service
public class ProfessionalResumeRendererService {

    private static final String FONT =
            "Arial";


    // =========================================================
    // DOCX
    // =========================================================

    public byte[] renderDocx(
            StructuredResume resume) {

        validate(resume);


        LayoutProfile profile =
                chooseLayout(
                        resume
                );


        try (
                XWPFDocument document =
                        new XWPFDocument();

                ByteArrayOutputStream output =
                        new ByteArrayOutputStream()
        ) {

            configurePage(
                    document,
                    profile
            );


            // =================================================
            // NAME
            // =================================================

            createName(
                    document,
                    resume.getName(),
                    profile
            );


            // =================================================
            // CONTACT
            // =================================================

            createContactLine(
                    document,
                    resume,
                    profile
            );


            // =================================================
            // CLICKABLE PROFILE LINKS
            // =================================================

            createProfileLinks(
                    document,
                    resume.getLinks(),
                    profile
            );


            createDivider(
                    document,
                    profile
            );


            // =================================================
            // CAREER OBJECTIVE
            // =================================================

            if (!safe(
                    resume.getCareerObjective()
            ).isBlank()) {

                createSection(
                        document,
                        "CAREER OBJECTIVE",
                        profile
                );


                createBody(
                        document,
                        resume.getCareerObjective(),
                        profile
                );
            }


            // =================================================
            // EDUCATION
            // =================================================

            if (resume.getEducation() != null
                    &&
                !resume.getEducation().isEmpty()) {

                createSection(
                        document,
                        "EDUCATION",
                        profile
                );


                for (EducationEntry entry :
                        resume.getEducation()) {

                    if (entry == null) {

                        continue;
                    }


                    createEntryTitle(
                            document,
                            entry.getInstitution(),
                            entry.getDuration(),
                            profile
                    );


                    String details =
                            joinNonBlank(
                                    entry.getQualification(),
                                    entry.getScore()
                            );


                    details =
                            joinNonBlank(
                                    details,
                                    entry.getLocation()
                            );


                    if (!details.isBlank()) {

                        createCompactBody(
                                document,
                                details,
                                profile
                        );
                    }
                }
            }


            // =================================================
            // WORK EXPERIENCE
            // =================================================

            if (resume.getExperience() != null
                    &&
                !resume.getExperience().isEmpty()) {

                createSection(
                        document,
                        "WORK EXPERIENCE",
                        profile
                );


                for (ExperienceEntry entry :
                        resume.getExperience()) {

                    if (entry == null) {

                        continue;
                    }


                    createEntryTitle(
                            document,
                            entry.getRole(),
                            entry.getDuration(),
                            profile
                    );


                    if (!safe(
                            entry.getCompany()
                    ).isBlank()) {

                        createMeta(
                                document,
                                entry.getCompany(),
                                profile
                        );
                    }


                    if (entry.getBullets() != null) {

                        for (String bullet :
                                entry.getBullets()) {

                            createBullet(
                                    document,
                                    bullet,
                                    profile
                            );
                        }
                    }
                }
            }


            // =================================================
            // TECHNICAL SKILLS
            // =================================================

            if (resume.getSkillCategories() != null
                    &&
                !resume.getSkillCategories().isEmpty()) {

                createSection(
                        document,
                        "TECHNICAL SKILLS",
                        profile
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


                    createSkillRow(
                            document,
                            name,
                            String.join(
                                    ", ",
                                    skills
                            ),
                            profile
                    );
                }
            }


            // =================================================
            // PROJECTS
            // =================================================

            if (resume.getProjects() != null
                    &&
                !resume.getProjects().isEmpty()) {

                createSection(
                        document,
                        "PROJECTS",
                        profile
                );


                for (ProjectEntry project :
                        resume.getProjects()) {

                    if (project == null) {

                        continue;
                    }


                    createProjectTitle(
                            document,
                            project.getName(),
                            profile
                    );


                    if (!safe(
                            project.getTechnologies()
                    ).isBlank()) {

                        createMeta(
                                document,
                                project.getTechnologies(),
                                profile
                        );
                    }


                    if (project.getBullets() != null) {

                        for (String bullet :
                                project.getBullets()) {

                            createBullet(
                                    document,
                                    bullet,
                                    profile
                            );
                        }
                    }
                }
            }


            // =================================================
            // RESEARCH
            // =================================================

            createSimpleListSection(
                    document,
                    "RESEARCH PUBLICATION",
                    resume.getResearchPublications(),
                    profile
            );


            // =================================================
            // CERTIFICATIONS
            // =================================================

            createSimpleListSection(
                    document,
                    "CERTIFICATIONS",
                    resume.getCertifications(),
                    profile
            );


            // =================================================
            // ACHIEVEMENTS
            // =================================================

            createSimpleListSection(
                    document,
                    "ACHIEVEMENTS",
                    resume.getAchievements(),
                    profile
            );


            document.write(
                    output
            );


            return output.toByteArray();


        } catch (Exception e) {

            throw new RuntimeException(
                    "DOCX generation failed: "
                    + e.getMessage(),
                    e
            );
        }
    }


    // =========================================================
    // CONTACT LINE
    // =========================================================

    private void createContactLine(
            XWPFDocument document,
            StructuredResume resume,
            LayoutProfile profile) {

        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setAlignment(
                ParagraphAlignment.CENTER
        );


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.headerLineAfter
        );


        boolean hasPrevious = false;


        // =====================================================
        // EMAIL
        // =====================================================

        String email =
                normalize(
                        resume.getEmail()
                );


        if (!email.isBlank()) {

            XWPFHyperlinkRun emailRun =
                    paragraph.createHyperlinkRun(
                            "mailto:" + email
                    );


            configureHyperlink(
                    emailRun,
                    profile.contactFont
            );


            emailRun.setText(
                    email
            );


            hasPrevious = true;
        }


        // =====================================================
        // PHONE
        // =====================================================

        String phone =
                normalize(
                        resume.getPhone()
                );


        if (!phone.isBlank()) {

            if (hasPrevious) {

                createSeparator(
                        paragraph,
                        profile.contactFont
                );
            }


            XWPFRun phoneRun =
                    paragraph.createRun();


            phoneRun.setText(
                    phone
            );


            phoneRun.setFontFamily(
                    FONT
            );


            phoneRun.setFontSize(
                    profile.contactFont
            );


            hasPrevious = true;
        }


        // =====================================================
        // LOCATION
        // =====================================================

        String location =
                normalize(
                        resume.getLocation()
                );


        if (!location.isBlank()) {

            if (hasPrevious) {

                createSeparator(
                        paragraph,
                        profile.contactFont
                );
            }


            XWPFRun locationRun =
                    paragraph.createRun();


            locationRun.setText(
                    location
            );


            locationRun.setFontFamily(
                    FONT
            );


            locationRun.setFontSize(
                    profile.contactFont
            );
        }
    }


    // =========================================================
    // CLICKABLE PROFILE LINKS
    // =========================================================

    private void createProfileLinks(
            XWPFDocument document,
            List<String> rawLinks,
            LayoutProfile profile) {

        List<ProfileLink> links =
                buildProfileLinks(
                        rawLinks
                );


        if (links.isEmpty()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setAlignment(
                ParagraphAlignment.CENTER
        );


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.headerLineAfter
        );


        for (int i = 0;
             i < links.size();
             i++) {

            ProfileLink link =
                    links.get(i);


            if (i > 0) {

                createSeparator(
                        paragraph,
                        profile.linkFont
                );
            }


            if (!link.url.isBlank()) {

                XWPFHyperlinkRun hyperlink =
                        paragraph.createHyperlinkRun(
                                link.url
                        );


                configureHyperlink(
                        hyperlink,
                        profile.linkFont
                );


                hyperlink.setText(
                        link.label
                );


            } else {

                XWPFRun run =
                        paragraph.createRun();


                run.setText(
                        link.label
                );


                run.setFontFamily(
                        FONT
                );


                run.setFontSize(
                        profile.linkFont
                );
            }
        }
    }


    private void configureHyperlink(
            XWPFHyperlinkRun run,
            int fontSize) {

        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                fontSize
        );


        /*
         * Black keeps the resume clean and ATS-safe.
         * Underline indicates that the text is clickable.
         */
        run.setColor(
                "000000"
        );


        run.setUnderline(
                UnderlinePatterns.SINGLE
        );
    }


    private void createSeparator(
            XWPFParagraph paragraph,
            int size) {

        XWPFRun separator =
                paragraph.createRun();


        separator.setText(
                "  |  "
        );


        separator.setFontFamily(
                FONT
        );


        separator.setFontSize(
                size
        );
    }


    // =========================================================
    // BUILD PROFILE LINKS
    // =========================================================

    private List<ProfileLink> buildProfileLinks(
            List<String> rawLinks) {

        List<ProfileLink> result =
                new ArrayList<>();


        if (rawLinks == null) {

            return result;
        }


        int genericIndex = 1;


        for (String raw : rawLinks) {

            String value =
                    normalize(
                            raw
                    );


            if (value.isBlank()) {

                continue;
            }


            String lower =
                    value.toLowerCase();


            String label;


            if (lower.contains(
                    "linkedin"
            )) {

                label =
                        "LinkedIn";

            } else if (lower.contains(
                    "github"
            )) {

                label =
                        "GitHub";

            } else if (lower.contains(
                    "leetcode"
            )) {

                label =
                        "LeetCode";

            } else if (lower.contains(
                    "hackerrank"
            )) {

                label =
                        "HackerRank";

            } else if (lower.contains(
                    "portfolio"
            )) {

                label =
                        "Portfolio";

            } else {

                label =
                        "Profile "
                        + genericIndex;

                genericIndex++;
            }


            String url =
                    normalizeUrl(
                            value
                    );


            boolean duplicate =
                    result.stream()
                            .anyMatch(
                                    item ->
                                            item.label
                                                .equalsIgnoreCase(
                                                        label
                                                )
                            );


            if (!duplicate) {

                result.add(
                        new ProfileLink(
                                label,
                                url
                        )
                );
            }
        }


        return result;
    }


    // =========================================================
    // URL NORMALIZATION
    // =========================================================

    private String normalizeUrl(
            String raw) {

        String text =
                normalize(
                        raw
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


        int whitespace =
                firstWhitespaceIndex(
                        url
                );


        if (whitespace > 0) {

            url =
                    url.substring(
                            0,
                            whitespace
                    );
        }


        while (
                url.endsWith("|")
                ||
                url.endsWith(",")
                ||
                url.endsWith(";")
                ||
                url.endsWith(")")
        ) {

            url =
                    url.substring(
                            0,
                            url.length() - 1
                    );
        }


        return url;
    }


    private int firstWhitespaceIndex(
            String text) {

        for (int i = 0;
             i < text.length();
             i++) {

            if (Character.isWhitespace(
                    text.charAt(i)
            )) {

                return i;
            }
        }


        return -1;
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


        if (density <= 42) {

            return LayoutProfile.spacious();
        }


        if (density <= 57) {

            return LayoutProfile.normal();
        }


        return LayoutProfile.compact();
    }


    private int estimateDensity(
            StructuredResume resume) {

        int units = 3;


        String objective =
                safe(
                    resume.getCareerObjective()
                );


        if (!objective.isBlank()) {

            units += 1;

            units += wrappedUnits(
                    objective,
                    95
            );
        }


        if (resume.getEducation() != null) {

            if (!resume.getEducation().isEmpty()) {

                units += 1;
            }


            for (EducationEntry entry :
                    resume.getEducation()) {

                if (entry != null) {

                    units += 2;
                }
            }
        }


        if (resume.getExperience() != null) {

            if (!resume.getExperience().isEmpty()) {

                units += 1;
            }


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
                                        100
                                );
                    }
                }
            }
        }


        if (resume.getSkillCategories() != null) {

            if (!resume.getSkillCategories().isEmpty()) {

                units += 1;
            }


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
                                105
                        );
            }
        }


        if (resume.getProjects() != null) {

            if (!resume.getProjects().isEmpty()) {

                units += 1;
            }


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
                                        100
                                );
                    }
                }
            }
        }


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
                            100
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
    // PAGE CONFIGURATION
    // =========================================================

    private void configurePage(
            XWPFDocument document,
            LayoutProfile profile) {

        CTSectPr section;


        if (document
                .getDocument()
                .getBody()
                .isSetSectPr()) {

            section =
                    document
                        .getDocument()
                        .getBody()
                        .getSectPr();

        } else {

            section =
                    document
                        .getDocument()
                        .getBody()
                        .addNewSectPr();
        }


        CTPageMar margins;


        if (section.isSetPgMar()) {

            margins =
                    section.getPgMar();

        } else {

            margins =
                    section.addNewPgMar();
        }


        margins.setTop(
                BigInteger.valueOf(
                        profile.topMargin
                )
        );


        margins.setBottom(
                BigInteger.valueOf(
                        profile.bottomMargin
                )
        );


        margins.setLeft(
                BigInteger.valueOf(
                        profile.sideMargin
                )
        );


        margins.setRight(
                BigInteger.valueOf(
                        profile.sideMargin
                )
        );
    }


    // =========================================================
    // NAME
    // =========================================================

    private void createName(
            XWPFDocument document,
            String value,
            LayoutProfile profile) {

        String name =
                normalize(
                        value
                );


        if (name.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setAlignment(
                ParagraphAlignment.CENTER
        );


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.nameAfter
        );


        XWPFRun run =
                paragraph.createRun();


        run.setText(
                name.toUpperCase()
        );


        run.setBold(
                true
        );


        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                profile.nameFont
        );
    }


    // =========================================================
    // DIVIDER
    // =========================================================

    private void createDivider(
            XWPFDocument document,
            LayoutProfile profile) {

        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setBorderBottom(
                Borders.SINGLE
        );


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.headerAfter
        );
    }


    // =========================================================
    // SECTION
    // =========================================================

    private void createSection(
            XWPFDocument document,
            String title,
            LayoutProfile profile) {

        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setSpacingBefore(
                profile.sectionBefore
        );


        paragraph.setSpacingAfter(
                profile.sectionAfter
        );


        paragraph.setBorderBottom(
                Borders.SINGLE
        );


        XWPFRun run =
                paragraph.createRun();


        run.setText(
                safe(title)
                    .toUpperCase()
        );


        run.setBold(
                true
        );


        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                profile.headingFont
        );
    }


    // =========================================================
    // ENTRY TITLE
    // =========================================================

    private void createEntryTitle(
            XWPFDocument document,
            String left,
            String right,
            LayoutProfile profile) {

        String leftText =
                normalize(
                        left
                );


        String rightText =
                normalize(
                        right
                );


        if (leftText.isBlank()
                &&
            rightText.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setSpacingBefore(
                profile.entryBefore
        );


        paragraph.setSpacingAfter(
                profile.entryAfter
        );


        XWPFRun leftRun =
                paragraph.createRun();


        leftRun.setText(
                leftText
        );


        leftRun.setBold(
                true
        );


        leftRun.setFontFamily(
                FONT
        );


        leftRun.setFontSize(
                profile.bodyFont
        );


        if (!rightText.isBlank()) {

            XWPFRun separator =
                    paragraph.createRun();


            separator.setText(
                    "  |  "
            );


            separator.setFontFamily(
                    FONT
            );


            separator.setFontSize(
                    profile.bodyFont
            );


            XWPFRun rightRun =
                    paragraph.createRun();


            rightRun.setText(
                    rightText
            );


            rightRun.setBold(
                    true
            );


            rightRun.setFontFamily(
                    FONT
            );


            rightRun.setFontSize(
                    profile.bodyFont
            );
        }
    }


    // =========================================================
    // PROJECT TITLE
    // =========================================================

    private void createProjectTitle(
            XWPFDocument document,
            String value,
            LayoutProfile profile) {

        String title =
                normalize(
                        value
                );


        if (title.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setSpacingBefore(
                profile.entryBefore
        );


        paragraph.setSpacingAfter(
                profile.entryAfter
        );


        XWPFRun run =
                paragraph.createRun();


        run.setText(
                title
        );


        run.setBold(
                true
        );


        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                profile.bodyFont
        );
    }


    // =========================================================
    // META
    // =========================================================

    private void createMeta(
            XWPFDocument document,
            String value,
            LayoutProfile profile) {

        String text =
                normalize(
                        value
                );


        if (text.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.metaAfter
        );


        XWPFRun run =
                paragraph.createRun();


        run.setText(
                text
        );


        run.setItalic(
                true
        );


        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                profile.metaFont
        );
    }


    // =========================================================
    // BODY
    // =========================================================

    private void createBody(
            XWPFDocument document,
            String value,
            LayoutProfile profile) {

        String text =
                normalize(
                        value
                );


        if (text.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setAlignment(
                ParagraphAlignment.BOTH
        );


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.bodyAfter
        );


        XWPFRun run =
                paragraph.createRun();


        run.setText(
                text
        );


        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                profile.bodyFont
        );
    }


    // =========================================================
    // COMPACT BODY
    // =========================================================

    private void createCompactBody(
            XWPFDocument document,
            String value,
            LayoutProfile profile) {

        String text =
                normalize(
                        value
                );


        if (text.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.compactBodyAfter
        );


        XWPFRun run =
                paragraph.createRun();


        run.setText(
                text
        );


        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                profile.bodyFont
        );
    }


    // =========================================================
    // BULLET
    // =========================================================

    private void createBullet(
            XWPFDocument document,
            String value,
            LayoutProfile profile) {

        String text =
                cleanBullet(
                        value
                );


        if (text.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setAlignment(
                ParagraphAlignment.BOTH
        );


        paragraph.setIndentationLeft(
                profile.bulletIndent
        );


        paragraph.setIndentationHanging(
                profile.bulletHanging
        );


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.bulletAfter
        );


        XWPFRun run =
                paragraph.createRun();


        run.setText(
                "• " + text
        );


        run.setFontFamily(
                FONT
        );


        run.setFontSize(
                profile.bodyFont
        );
    }


    // =========================================================
    // SKILL ROW
    // =========================================================

    private void createSkillRow(
            XWPFDocument document,
            String category,
            String skills,
            LayoutProfile profile) {

        String categoryText =
                normalize(
                        category
                );


        String skillsText =
                normalize(
                        skills
                );


        if (categoryText.isBlank()
                ||
            skillsText.isBlank()) {

            return;
        }


        XWPFParagraph paragraph =
                document.createParagraph();


        paragraph.setSpacingBefore(
                0
        );


        paragraph.setSpacingAfter(
                profile.skillAfter
        );


        XWPFRun label =
                paragraph.createRun();


        label.setText(
                categoryText
                + ": "
        );


        label.setBold(
                true
        );


        label.setFontFamily(
                FONT
        );


        label.setFontSize(
                profile.bodyFont
        );


        XWPFRun value =
                paragraph.createRun();


        value.setText(
                skillsText
        );


        value.setFontFamily(
                FONT
        );


        value.setFontSize(
                profile.bodyFont
        );
    }


    // =========================================================
    // SIMPLE LIST SECTION
    // =========================================================

    private void createSimpleListSection(
            XWPFDocument document,
            String title,
            List<String> values,
            LayoutProfile profile) {

        List<String> clean =
                cleanList(
                        values
                );


        if (clean.isEmpty()) {

            return;
        }


        createSection(
                document,
                title,
                profile
        );


        for (String value : clean) {

            createBullet(
                    document,
                    value,
                    profile
            );
        }
    }


    // =========================================================
    // FALLBACK PDF
    // =========================================================

    public byte[] renderPdf(
            StructuredResume resume) {

        validate(resume);


        try (
                PDDocument document =
                        new PDDocument();

                ByteArrayOutputStream output =
                        new ByteArrayOutputStream()
        ) {

            PDPage page =
                    new PDPage(
                            PDRectangle.A4
                    );


            document.addPage(
                    page
            );


            PDType1Font regular =
                    new PDType1Font(
                            Standard14Fonts
                                .FontName
                                .HELVETICA
                    );


            PDType1Font bold =
                    new PDType1Font(
                            Standard14Fonts
                                .FontName
                                .HELVETICA_BOLD
                    );


            try (
                    PDPageContentStream stream =
                            new PDPageContentStream(
                                    document,
                                    page
                            )
            ) {

                float y =
                        page.getMediaBox()
                                .getHeight()
                        - 50;


                stream.beginText();


                stream.setFont(
                        bold,
                        16
                );


                stream.newLineAtOffset(
                        45,
                        y
                );


                stream.showText(
                        pdfSafe(
                                safe(
                                    resume.getName()
                                ).toUpperCase()
                        )
                );


                stream.endText();


                y -= 25;


                List<String> header =
                        new ArrayList<>();


                addIfPresent(
                        header,
                        resume.getEmail()
                );


                addIfPresent(
                        header,
                        resume.getPhone()
                );


                addIfPresent(
                        header,
                        resume.getLocation()
                );


                stream.beginText();


                stream.setFont(
                        regular,
                        9
                );


                stream.newLineAtOffset(
                        45,
                        y
                );


                stream.showText(
                        pdfSafe(
                                String.join(
                                        " | ",
                                        header
                                )
                        )
                );


                stream.endText();
            }


            document.save(
                    output
            );


            return output.toByteArray();


        } catch (Exception e) {

            throw new RuntimeException(
                    "Fallback PDF generation failed: "
                    + e.getMessage(),
                    e
            );
        }
    }


    // =========================================================
    // HELPERS
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


    private void addIfPresent(
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


    private List<String> cleanList(
            List<String> values) {

        List<String> result =
                new ArrayList<>();


        if (values == null) {

            return result;
        }


        for (String value : values) {

            String clean =
                    normalize(
                            value
                    );


            if (!clean.isBlank()
                    &&
                !result.contains(
                        clean
                )) {

                result.add(
                        clean
                );
            }
        }


        return result;
    }


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


    private String pdfSafe(
            String value) {

        return normalize(
                value
        )
                .replaceAll(
                        "[^\\x20-\\x7E]",
                        ""
                );
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }


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
    // PROFILE LINK
    // =========================================================

    private static class ProfileLink {

        private final String label;

        private final String url;


        private ProfileLink(
                String label,
                String url) {

            this.label =
                    label;

            this.url =
                    url;
        }
    }


    // =========================================================
    // LAYOUT PROFILE
    // =========================================================

    private static class LayoutProfile {

        private int bodyFont;

        private int metaFont;

        private int nameFont;

        private int contactFont;

        private int linkFont;

        private int headingFont;


        private long topMargin;

        private long bottomMargin;

        private long sideMargin;


        private int nameAfter;

        private int headerLineAfter;

        private int headerAfter;


        private int sectionBefore;

        private int sectionAfter;


        private int entryBefore;

        private int entryAfter;


        private int metaAfter;

        private int bodyAfter;

        private int compactBodyAfter;


        private int bulletAfter;

        private int bulletIndent;

        private int bulletHanging;


        private int skillAfter;


        private static LayoutProfile spacious() {

            LayoutProfile p =
                    new LayoutProfile();


            p.bodyFont = 10;

            p.metaFont = 9;

            p.nameFont = 19;

            p.contactFont = 9;

            p.linkFont = 9;

            p.headingFont = 11;


            p.topMargin = 650;

            p.bottomMargin = 650;

            p.sideMargin = 720;


            p.nameAfter = 35;

            p.headerLineAfter = 12;

            p.headerAfter = 30;


            p.sectionBefore = 80;

            p.sectionAfter = 30;


            p.entryBefore = 18;

            p.entryAfter = 6;


            p.metaAfter = 8;

            p.bodyAfter = 15;

            p.compactBodyAfter = 12;


            p.bulletAfter = 8;

            p.bulletIndent = 300;

            p.bulletHanging = 150;


            p.skillAfter = 10;


            return p;
        }


        private static LayoutProfile normal() {

            LayoutProfile p =
                    new LayoutProfile();


            p.bodyFont = 10;

            p.metaFont = 9;

            p.nameFont = 18;

            p.contactFont = 9;

            p.linkFont = 8;

            p.headingFont = 10;


            p.topMargin = 500;

            p.bottomMargin = 500;

            p.sideMargin = 650;


            p.nameAfter = 18;

            p.headerLineAfter = 5;

            p.headerAfter = 15;


            p.sectionBefore = 45;

            p.sectionAfter = 15;


            p.entryBefore = 8;

            p.entryAfter = 2;


            p.metaAfter = 4;

            p.bodyAfter = 7;

            p.compactBodyAfter = 5;


            p.bulletAfter = 3;

            p.bulletIndent = 280;

            p.bulletHanging = 140;


            p.skillAfter = 4;


            return p;
        }


        private static LayoutProfile compact() {

            LayoutProfile p =
                    new LayoutProfile();


            p.bodyFont = 9;

            p.metaFont = 8;

            p.nameFont = 17;

            p.contactFont = 8;

            p.linkFont = 8;

            p.headingFont = 10;


            p.topMargin = 360;

            p.bottomMargin = 360;

            p.sideMargin = 575;


            p.nameAfter = 6;

            p.headerLineAfter = 0;

            p.headerAfter = 5;


            p.sectionBefore = 20;

            p.sectionAfter = 5;


            p.entryBefore = 2;

            p.entryAfter = 0;


            p.metaAfter = 0;

            p.bodyAfter = 2;

            p.compactBodyAfter = 0;


            p.bulletAfter = 0;

            p.bulletIndent = 250;

            p.bulletHanging = 125;


            p.skillAfter = 0;


            return p;
        }
    }
}