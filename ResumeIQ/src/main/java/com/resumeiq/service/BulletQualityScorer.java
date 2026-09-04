package com.resumeiq.service;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.resumeiq.util.SkillCatalog;

@Service
public class BulletQualityScorer {

    private static final Pattern NUMBER_PATTERN =
            Pattern.compile(
                    ".*\\b\\d[\\d,]*(?:\\.\\d+)?(?:%|\\+|x)?\\b.*"
            );


    private static final Set<String> ACTION_VERBS =
            Set.of(
                    "achieved",
                    "analyzed",
                    "automated",
                    "built",
                    "created",
                    "designed",
                    "developed",
                    "engineered",
                    "evaluated",
                    "implemented",
                    "improved",
                    "integrated",
                    "led",
                    "optimized",
                    "performed",
                    "processed",
                    "reduced",
                    "resolved",
                    "secured",
                    "trained",
                    "tested",
                    "deployed",
                    "managed",
                    "configured",
                    "contributed"
            );


    private static final Set<String> WEAK_PHRASES =
            Set.of(
                    "responsible for",
                    "worked on",
                    "helped with",
                    "participated in",
                    "various tasks",
                    "multiple tasks",
                    "involved in",
                    "assisted with"
            );


    public int score(
            String bullet) {

        String value =
                cleanBullet(
                        bullet
                );


        if (value.isBlank()) {

            return 0;
        }


        int score =
                35;


        // =====================================================
        // ACTION VERB
        // =====================================================

        String firstWord =
                value
                    .split("\\s+")[0]
                    .toLowerCase(
                            Locale.ROOT
                    )
                    .replaceAll(
                            "[^a-z]",
                            ""
                    );


        if (ACTION_VERBS.contains(
                firstWord
        )) {

            score += 15;

        } else {

            score += 4;
        }


        // =====================================================
        // QUANTIFICATION
        // =====================================================

        if (NUMBER_PATTERN
                .matcher(value)
                .matches()) {

            score += 18;
        }


        // =====================================================
        // TECHNOLOGY / SKILL SPECIFICITY
        // =====================================================

        int skillCount =
                SkillCatalog
                    .findSkills(value)
                    .size();


        if (skillCount >= 3) {

            score += 15;

        } else if (skillCount == 2) {

            score += 12;

        } else if (skillCount == 1) {

            score += 7;
        }


        // =====================================================
        // IDEAL BULLET LENGTH
        // =====================================================

        int words =
                wordCount(
                        value
                );


        if (words >= 10
                &&
            words <= 28) {

            score += 10;

        } else if (words >= 7
                &&
                   words <= 34) {

            score += 5;

        } else if (words > 40) {

            score -= 8;
        }


        // =====================================================
        // WEAK LANGUAGE
        // =====================================================

        String lower =
                value.toLowerCase(
                        Locale.ROOT
                );


        for (String weak :
                WEAK_PHRASES) {

            if (lower.contains(
                    weak
            )) {

                score -= 12;
            }
        }


        return Math.max(
                0,
                Math.min(
                        100,
                        score
                )
        );
    }


    /*
     * Bullets at/above this score are already good.
     *
     * AI should leave them alone.
     */
    public boolean isStrong(
            String bullet) {

        return score(bullet)
                >= 76;
    }


    /*
     * Candidate must be meaningfully better.
     *
     * +1 score is not enough reason to modify
     * a person's resume.
     */
    public boolean isMeaningfulImprovement(
            String original,
            String candidate) {

        int before =
                score(
                        original
                );


        int after =
                score(
                        candidate
                );


        return after
                >= before + 5;
    }


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
}