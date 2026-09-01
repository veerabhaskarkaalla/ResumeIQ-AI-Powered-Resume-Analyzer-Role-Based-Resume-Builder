package com.resumeiq.service;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.resumeiq.util.SkillCatalog;

@Service
public class FactPreservationValidator {

    private static final Pattern NUMBER_PATTERN =
            Pattern.compile(
                    "(?<![A-Za-z0-9])"
                    + "\\$?"
                    + "\\d[\\d,]*"
                    + "(?:\\.\\d+)?"
                    + "(?:%|\\+|x)?"
                    + "(?![A-Za-z0-9])"
            );


    /*
     * Strict validation for experience/project bullets.
     *
     * Candidate must:
     * - not invent numbers
     * - not remove numbers
     * - not invent skills
     * - not remove skills
     * - retain sufficient factual wording
     */
    public boolean isSafeRewrite(
            String original,
            String candidate,
            String extraEvidence) {

        String source =
                cleanBullet(
                        original
                );


        String rewritten =
                cleanBullet(
                        candidate
                );


        if (source.isBlank()
                || rewritten.isBlank()) {

            return false;
        }


        if (rewritten.length() > 500) {

            return false;
        }


        String evidence =
                source
                + " "
                + safe(extraEvidence);


        // =====================================================
        // NUMBERS — NO ADDITIONS, NO LOSSES
        // =====================================================

        Set<String> sourceNumbers =
                extractNumbers(
                        source
                );


        Set<String> candidateNumbers =
                extractNumbers(
                        rewritten
                );


        /*
         * Candidate cannot introduce a new metric.
         */
        if (!extractNumbers(evidence)
                .containsAll(
                        candidateNumbers
                )) {

            return false;
        }


        /*
         * Candidate cannot remove original metrics.
         *
         * Example:
         * original = 94% accuracy, 91% precision
         * candidate = 94% accuracy
         *
         * -> reject
         */
        if (!candidateNumbers
                .containsAll(
                        sourceNumbers
                )) {

            return false;
        }


        // =====================================================
        // SKILLS — NO ADDITIONS, NO LOSSES
        // =====================================================

        Set<String> originalSkills =
                canonicalSkills(
                        source
                );


        Set<String> evidenceSkills =
                canonicalSkills(
                        evidence
                );


        Set<String> candidateSkills =
                canonicalSkills(
                        rewritten
                );


        /*
         * No newly invented technology.
         */
        if (!evidenceSkills
                .containsAll(
                        candidateSkills
                )) {

            return false;
        }


        /*
         * Don't delete technologies that were
         * explicitly present in this original bullet.
         */
        if (!candidateSkills
                .containsAll(
                        originalSkills
                )) {

            return false;
        }


        // =====================================================
        // LENGTH SAFETY
        // =====================================================

        int originalWords =
                wordCount(source);


        int candidateWords =
                wordCount(rewritten);


        if (candidateWords < 5) {

            return false;
        }


        /*
         * Prevent AI from turning one concise bullet
         * into a paragraph.
         */
        int maximum =
                Math.max(
                        32,
                        originalWords + 10
                );


        if (candidateWords > maximum) {

            return false;
        }


        /*
         * Prevent over-compression that drops meaning.
         */
        if (originalWords >= 10
                &&
            candidateWords
                < Math.ceil(
                    originalWords * 0.60
                )) {

            return false;
        }


        // =====================================================
        // FACTUAL WORD RETENTION
        // =====================================================

        Set<String> originalWordsSet =
                meaningfulWords(
                        source
                );


        Set<String> candidateWordsSet =
                meaningfulWords(
                        rewritten
                );


        if (originalWordsSet.isEmpty()) {

            return true;
        }


        int retained =
                0;


        for (String word :
                originalWordsSet) {

            if (candidateWordsSet
                    .contains(word)) {

                retained++;
            }
        }


        double retention =
                (double) retained
                /
                originalWordsSet.size();


        /*
         * Conservative:
         * majority of factual vocabulary should remain.
         */
        return retained >= 2
                &&
                retention >= 0.40;
    }


    /*
     * More flexible validation for Career Objective.
     *
     * Summary may choose a subset of skills,
     * therefore we only reject NEW unsupported skills/numbers.
     */
    public boolean isSupportedSummary(
            String completeResumeEvidence,
            String candidate,
            String targetRole) {

        if (candidate == null
                ||
            candidate.isBlank()) {

            return false;
        }


        String cleaned =
                candidate
                    .replaceAll(
                        "\\s+",
                        " "
                    )
                    .trim();


        int words =
                wordCount(cleaned);


        if (words < 15
                ||
            words > 65) {

            return false;
        }


        String evidence =
                safe(
                    completeResumeEvidence
                )
                + " "
                + safe(
                    targetRole
                );


        Set<String> evidenceNumbers =
                extractNumbers(
                        evidence
                );


        Set<String> candidateNumbers =
                extractNumbers(
                        cleaned
                );


        if (!evidenceNumbers
                .containsAll(
                        candidateNumbers
                )) {

            return false;
        }


        Set<String> evidenceSkills =
                canonicalSkills(
                        evidence
                );


        Set<String> candidateSkills =
                canonicalSkills(
                        cleaned
                );


        return evidenceSkills
                .containsAll(
                        candidateSkills
                );
    }


    private Set<String> canonicalSkills(
            String text) {

        Set<String> result =
                new LinkedHashSet<>();


        if (text == null
                ||
            text.isBlank()) {

            return result;
        }


        for (String skill :
                SkillCatalog.findSkills(
                        text
                )) {

            String canonical =
                    SkillCatalog
                            .resolveCanonicalSkill(
                                    skill
                            );


            if (canonical != null) {

                result.add(
                        canonical.toLowerCase(
                                Locale.ROOT
                        )
                );

            } else if (skill != null) {

                result.add(
                        skill
                            .toLowerCase(
                                Locale.ROOT
                            )
                            .trim()
                );
            }
        }


        return result;
    }


    private Set<String> extractNumbers(
            String value) {

        Set<String> numbers =
                new LinkedHashSet<>();


        if (value == null) {

            return numbers;
        }


        Matcher matcher =
                NUMBER_PATTERN.matcher(
                        value
                );


        while (matcher.find()) {

            numbers.add(
                    normalizeNumber(
                            matcher.group()
                    )
            );
        }


        return numbers;
    }


    private String normalizeNumber(
            String value) {

        return safe(value)
                .toLowerCase(
                        Locale.ROOT
                )
                .replace(
                        ",",
                        ""
                )
                .trim();
    }


    private Set<String> meaningfulWords(
            String value) {

        Set<String> result =
                new LinkedHashSet<>();


        if (value == null) {

            return result;
        }


        String normalized =
                value
                    .toLowerCase(
                        Locale.ROOT
                    )
                    .replaceAll(
                        "[^a-z0-9+#.]+",
                        " "
                    );


        for (String word :
                normalized.split("\\s+")) {

            if (word.length() < 3) {
                continue;
            }


            if (STOP_WORDS.contains(
                    word
            )) {

                continue;
            }


            if (word.matches(
                    "\\d+(?:\\.\\d+)?"
            )) {

                continue;
            }


            result.add(
                    word
            );
        }


        return result;
    }


    private static final Set<String> STOP_WORDS =
            Set.of(
                    "the",
                    "and",
                    "for",
                    "with",
                    "from",
                    "into",
                    "using",
                    "used",
                    "that",
                    "this",
                    "these",
                    "those",
                    "was",
                    "were",
                    "are",
                    "is",
                    "to",
                    "of",
                    "in",
                    "on",
                    "at",
                    "by",
                    "as",
                    "an",
                    "a",
                    "across",
                    "through",
                    "built",
                    "developed",
                    "implemented",
                    "created",
                    "performed",
                    "worked",
                    "applied",
                    "contributed",
                    "leveraged",
                    "utilized"
            );


    private int wordCount(
            String value) {

        if (value == null
                ||
            value.isBlank()) {

            return 0;
        }


        return value
                .trim()
                .split("\\s+")
                .length;
    }


    private String cleanBullet(
            String value) {

        return safe(value)
                .trim()
                .replaceFirst(
                        "^[•●▪◦*-]\\s*",
                        ""
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