package com.resumeiq.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.resumeiq.util.SkillCatalog;

@Component
public class HallucinationGuard {

    private static final Set<String> STOP_WORDS =
            new HashSet<>(
                    Arrays.asList(
                            "with",
                            "from",
                            "that",
                            "this",
                            "have",
                            "has",
                            "using",
                            "used",
                            "strong",
                            "good",
                            "knowledge",
                            "experience",
                            "experienced",
                            "candidate",
                            "development",
                            "developed",
                            "skills",
                            "skill",
                            "work",
                            "working",
                            "project",
                            "projects",
                            "built",
                            "created",
                            "implemented",
                            "designed"
                    )
            );


    public boolean isClaimSupported(
            String claim,
            String resumeText) {

        if (claim == null
                || claim.isBlank()
                || resumeText == null
                || resumeText.isBlank()) {

            return false;
        }


        List<String> claimSkills =
                SkillCatalog.findSkills(claim);


        if (!claimSkills.isEmpty()) {

            for (String skill : claimSkills) {

                if (!SkillCatalog.containsSkill(
                        resumeText,
                        skill)) {

                    return false;
                }
            }
        }


        return hasMeaningfulTextOverlap(
                claim,
                resumeText
        )
        || !claimSkills.isEmpty();
    }


    public List<String> getVerifiedStrengths(
            List<String> strengths,
            String resumeText) {

        return getVerifiedClaims(
                strengths,
                resumeText
        );
    }


    public List<String> getRejectedStrengths(
            List<String> strengths,
            String resumeText) {

        return getRejectedClaims(
                strengths,
                resumeText
        );
    }


    public List<String> getVerifiedClaims(
            List<String> claims,
            String resumeText) {

        List<String> verified =
                new ArrayList<>();


        if (claims == null) {
            return verified;
        }


        for (String claim : claims) {

            if (isClaimSupported(
                    claim,
                    resumeText)) {

                verified.add(claim);
            }
        }


        return verified;
    }


    public List<String> getRejectedClaims(
            List<String> claims,
            String resumeText) {

        List<String> rejected =
                new ArrayList<>();


        if (claims == null) {
            return rejected;
        }


        for (String claim : claims) {

            if (!isClaimSupported(
                    claim,
                    resumeText)) {

                rejected.add(claim);
            }
        }


        return rejected;
    }


    private boolean hasMeaningfulTextOverlap(
            String claim,
            String resumeText) {

        String normalizedResume =
                normalize(resumeText);


        String[] words =
                normalize(claim)
                        .split("\\s+");


        int meaningfulWords = 0;

        int matchedWords = 0;


        for (String word : words) {

            if (word.length() < 4
                    || STOP_WORDS.contains(word)) {

                continue;
            }


            meaningfulWords++;


            if (normalizedResume.contains(word)) {

                matchedWords++;
            }
        }


        if (meaningfulWords == 0) {
            return false;
        }


        if (meaningfulWords == 1) {
            return matchedWords == 1;
        }


        double ratio =
                matchedWords
                / (double) meaningfulWords;


        return matchedWords >= 2
                && ratio >= 0.40;
    }


    private String normalize(String text) {

        return text
                .toLowerCase()
                .replaceAll(
                        "[^a-z0-9+#. ]",
                        " "
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }
}