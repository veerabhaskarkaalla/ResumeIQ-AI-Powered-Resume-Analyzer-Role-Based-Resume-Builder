package com.resumeiq.ai;

import org.springframework.stereotype.Service;

@Service
public class PromptService {

    public String buildSemanticAnalysisPrompt(
            String resumeText,
            String company,
            String role,
            String jobDescription) {

        return """
                Analyze the candidate resume against the job description.

                RULES:
                1. Use only facts explicitly supported by the resume.
                2. Never invent skills, experience, projects,
                   certifications or achievements.
                3. Semantic matching is allowed.
                4. Missing evidence must be listed as a gap.
                5. semanticMatchScore must be between 0 and 100.
                6. Maximum 5 items in each list.
                7. Return only structured JSON.

                TARGET COMPANY:
                %s

                TARGET ROLE:
                %s

                JOB DESCRIPTION:
                %s

                RESUME:
                %s
                """.formatted(
                        safe(company),
                        safe(role),
                        safe(jobDescription),
                        safe(resumeText)
                );
    }


    public String buildOptimizationPrompt(
            String resumeText,
            String company,
            String role,
            String jobDescription) {

        return """
                You are a fact-safe professional resume optimizer.

                Optimize the resume for the target role.

                STRICT RULES:

                1. Use ONLY facts that already exist in the resume.

                2. NEVER invent:
                   - skills
                   - technologies
                   - companies
                   - experience
                   - projects
                   - metrics
                   - certifications
                   - responsibilities

                3. You may improve wording and use stronger action verbs.

                4. You may use job-description terminology only when
                   the resume already provides evidence for that concept.

                5. NEVER create fake numbers or percentages.

                6. If a JD skill has no resume evidence,
                   put it in missingSkillsNotAdded.

                7. skillsToHighlight must contain only skills
                   already supported by the resume.

                8. Keep bullets concise and ATS-friendly.

                9. Maximum:
                   - 4 experience bullets
                   - 4 project bullets
                   - 8 skills
                   - 8 missing skills
                   - 6 changes

                10. Return only structured JSON.


                TARGET COMPANY:
                %s

                TARGET ROLE:
                %s


                JOB DESCRIPTION:
                %s


                ORIGINAL RESUME:
                %s
                """.formatted(
                        safe(company),
                        safe(role),
                        safe(jobDescription),
                        safe(resumeText)
                );
    }


    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }
}