package com.resumeiq.ai;

import java.util.List;

import org.springframework.stereotype.Service;

import com.resumeiq.dto.BulletOptimizationRequest;

@Service
public class StructuredPromptService {

    public String buildBulletPrompt(
            BulletOptimizationRequest request) {

        StringBuilder prompt =
                new StringBuilder();


        prompt.append(
                """
                You are a production resume optimization engine.

                Rewrite ONLY the supplied resume bullets.

                STRICT FACT-SAFETY RULES:

                1. Never invent technologies.
                2. Never invent metrics or numbers.
                3. Never invent responsibilities.
                4. Never invent achievements.
                5. Never invent tools.
                6. Never invent business impact.
                7. Never add a job-description skill unless that exact evidence exists in the source bullet.
                8. Preserve all factual meaning.
                9. Use the job description only to improve wording and relevance.
                10. Do not change company, project, role, dates or technologies.
                11. Keep each rewrite concise and ATS-friendly.
                12. Prefer strong action verbs.
                13. Each rewrite must correspond to exactly one original sourceIndex.
                14. Do not combine multiple original bullets.
                15. Do not create new bullets.
                16. Maximum 30 words per rewritten bullet.

                """
        );


        prompt.append(
                "ENTRY TYPE: "
        );

        prompt.append(
                safe(request.getEntryType())
        );

        prompt.append("\n");


        prompt.append(
                "TITLE: "
        );

        prompt.append(
                safe(request.getTitle())
        );

        prompt.append("\n");


        prompt.append(
                "ORGANIZATION: "
        );

        prompt.append(
                safe(request.getOrganization())
        );

        prompt.append("\n");


        prompt.append(
                "DURATION: "
        );

        prompt.append(
                safe(request.getDuration())
        );

        prompt.append("\n");


        prompt.append(
                "TECHNOLOGIES: "
        );

        prompt.append(
                safe(request.getTechnologies())
        );

        prompt.append("\n\n");


        prompt.append(
                "TARGET COMPANY: "
        );

        prompt.append(
                safe(request.getTargetCompany())
        );

        prompt.append("\n");


        prompt.append(
                "TARGET ROLE: "
        );

        prompt.append(
                safe(request.getTargetRole())
        );

        prompt.append("\n\n");


        prompt.append(
                "JOB DESCRIPTION:\n"
        );

        prompt.append(
                limit(
                        request.getJobDescription(),
                        4500
                )
        );

        prompt.append("\n\n");


        prompt.append(
                "ORIGINAL BULLETS:\n"
        );


        List<String> bullets =
                request.getOriginalBullets();


        if (bullets != null) {

            for (int i = 0;
                 i < bullets.size();
                 i++) {

                prompt.append("[");
                prompt.append(i);
                prompt.append("] ");

                prompt.append(
                        cleanBullet(
                                bullets.get(i)
                        )
                );

                prompt.append("\n");
            }
        }


        prompt.append(
                """

                Return ONLY structured JSON matching the required schema.

                Example logical format:

                {
                  "rewrites": [
                    {
                      "sourceIndex": 0,
                      "text": "Improved fact-safe version of source bullet 0."
                    }
                  ]
                }

                Every sourceIndex must refer to the corresponding original bullet.
                """
        );


        return prompt.toString();
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


    private String limit(
            String value,
            int maxLength) {

        if (value == null) {
            return "";
        }


        String cleaned =
                value.trim();


        if (cleaned.length()
                <= maxLength) {

            return cleaned;
        }


        return cleaned.substring(
                0,
                maxLength
        );
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }
}