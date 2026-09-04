package com.resumeiq.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.resumeiq.util.SkillCatalog;

@Service
public class EvidenceValidator {

    private static final Pattern NUMBER_PATTERN =
            Pattern.compile(
                    "(?i)"
                    + "(?<![A-Za-z0-9])"
                    + "\\$?"
                    + "\\d[\\d,]*"
                    + "(?:\\.\\d+)?"
                    + "(?:%|\\+|x)?"
                    + "(?![A-Za-z0-9])"
            );


    private static final Set<String>
            STOP_WORDS =
            Set.of(
                    "the",
                    "and",
                    "for",
                    "with",
                    "from",
                    "using",
                    "used",
                    "into",
                    "across",
                    "through",
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
                    "a",
                    "an",
                    "built",
                    "developed",
                    "created",
                    "implemented",
                    "designed",
                    "worked",
                    "applied",
                    "performed",
                    "improved",
                    "optimized",
                    "leveraged",
                    "utilized",
                    "delivered"
            );


    public boolean isRewriteSupported(
            String originalBullet,
            String rewrittenBullet,
            String extraEvidence) {

        String original =
                cleanBullet(
                        originalBullet
                );


        String rewritten =
                cleanBullet(
                        rewrittenBullet
                );


        if (original.isBlank()
                ||
            rewritten.isBlank()) {

            return false;
        }


        if (rewritten.length() > 450) {

            return false;
        }


        String evidence =
                original
                + " "
                + safe(extraEvidence);


        // ==========================================
        // 1. NO NEW SKILLS / TECHNOLOGIES
        // ==========================================

        for (String skill :
                SkillCatalog.findSkills(
                        rewritten
                )) {

            if (!SkillCatalog.containsSkill(
                    evidence,
                    skill
            )) {

                return false;
            }
        }


        // ==========================================
        // 2. NO NEW NUMBERS / METRICS
        // ==========================================

        Set<String> sourceNumbers =
                extractNumbers(
                        evidence
                );


        Set<String> candidateNumbers =
                extractNumbers(
                        rewritten
                );


        if (!sourceNumbers.containsAll(
                candidateNumbers
        )) {

            return false;
        }


        // ==========================================
        // 3. FACTUAL WORD OVERLAP
        // ==========================================

        Set<String> sourceWords =
                meaningfulWords(
                        evidence
                );


        Set<String> candidateWords =
                meaningfulWords(
                        rewritten
                );


        if (candidateWords.isEmpty()) {

            return false;
        }


        int matches = 0;


        for (String word :
                candidateWords) {

            if (sourceWords.contains(
                    word
            )) {

                matches++;
            }
        }


        if (candidateWords.size() <= 3) {

            return matches >= 1;
        }


        double overlap =
                (double) matches
                /
                candidateWords.size();


        /*
         * Conservative production threshold.
         *
         * Rewrite can use stronger action verbs,
         * but factual nouns should overlap.
         */
        return matches >= 2
                &&
                overlap >= 0.25;
    }


    private Set<String> extractNumbers(
            String text) {

        Set<String> values =
                new HashSet<>();


        if (text == null) {

            return values;
        }


        Matcher matcher =
                NUMBER_PATTERN.matcher(
                        text
                );


        while (matcher.find()) {

            values.add(
                    normalizeNumber(
                            matcher.group()
                    )
            );
        }


        return values;
    }


    private String normalizeNumber(
            String value) {

        return value
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
            String text) {

        Set<String> result =
                new HashSet<>();


        if (text == null) {

            return result;
        }


        String normalized =
                text
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
                    "\\d+"
            )) {

                continue;
            }


            result.add(
                    word
            );
        }


        return result;
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
                );
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}