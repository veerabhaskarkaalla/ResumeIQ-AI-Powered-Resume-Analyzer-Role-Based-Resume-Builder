package com.resumeiq.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.resumeiq.ai.AiService;
import com.resumeiq.ai.HallucinationGuard;
import com.resumeiq.ats.JobDescriptionAnalyzer;
import com.resumeiq.dto.AiOptimizationCandidate;
import com.resumeiq.dto.JobDescriptionAnalysis;
import com.resumeiq.dto.JobDescriptionRequest;
import com.resumeiq.dto.ResumeOptimizationRequest;
import com.resumeiq.dto.ResumeOptimizationResult;
import com.resumeiq.entity.Resume;
import com.resumeiq.util.SkillCatalog;

@Service
public class ResumeOptimizerService {

    private final ResumeService resumeService;

    private final AiService aiService;

    private final HallucinationGuard
            hallucinationGuard;

    private final JobDescriptionAnalyzer
            jobDescriptionAnalyzer;


    public ResumeOptimizerService(
            ResumeService resumeService,
            AiService aiService,
            HallucinationGuard hallucinationGuard,
            JobDescriptionAnalyzer jobDescriptionAnalyzer) {

        this.resumeService =
                resumeService;

        this.aiService =
                aiService;

        this.hallucinationGuard =
                hallucinationGuard;

        this.jobDescriptionAnalyzer =
                jobDescriptionAnalyzer;
    }


    public ResumeOptimizationResult optimize(
            ResumeOptimizationRequest request) {

        validate(
                request
        );


        Resume resume =
                resumeService.getResume(
                        request.getResumeId()
                );


        String originalResumeText =
                safe(
                        resume.getExtractedText()
                );


        // ==========================================
        // AI CONTENT OPTIMIZATION
        // ==========================================

        AiOptimizationCandidate candidate =
                aiService.generateOptimization(
                        request
                );


        if (candidate == null) {

            throw new RuntimeException(
                    "AI optimization returned no result"
            );
        }


        // ==========================================
        // VERIFY EXPERIENCE BULLETS
        // ==========================================

        List<String> verifiedExperience =
                hallucinationGuard
                        .getVerifiedClaims(
                                safeList(
                                        candidate
                                                .getExperienceBullets()
                                ),
                                originalResumeText
                        );


        List<String> rejectedExperience =
                hallucinationGuard
                        .getRejectedClaims(
                                safeList(
                                        candidate
                                                .getExperienceBullets()
                                ),
                                originalResumeText
                        );


        // ==========================================
        // VERIFY PROJECT BULLETS
        // ==========================================

        List<String> verifiedProjects =
                hallucinationGuard
                        .getVerifiedClaims(
                                safeList(
                                        candidate
                                                .getProjectBullets()
                                ),
                                originalResumeText
                        );


        List<String> rejectedProjects =
                hallucinationGuard
                        .getRejectedClaims(
                                safeList(
                                        candidate
                                                .getProjectBullets()
                                ),
                                originalResumeText
                        );


        // ==========================================
        // JOB DESCRIPTION
        // ==========================================

        JobDescriptionAnalysis jobAnalysis =
                analyzeJobDescription(
                        request
                );


        // ==========================================
        // DETERMINISTIC SKILL MATCHING
        // ==========================================

        List<String> jobSkills =
                collectJobSkills(
                        jobAnalysis
                );


        List<String> skillsToHighlight =
                findMatchedSkills(
                        jobSkills,
                        originalResumeText
                );


        List<String> missingSkills =
                findMissingSkills(
                        jobSkills,
                        originalResumeText
                );


        // ==========================================
        // SUMMARY VALIDATION
        // ==========================================

        String optimizedSummary =
                verifySummary(
                        candidate
                                .getProfessionalSummary(),
                        originalResumeText
                );


        // ==========================================
        // RESULT
        // ==========================================

        ResumeOptimizationResult result =
                new ResumeOptimizationResult();


        result.setResumeId(
                request.getResumeId()
        );


        result.setCompany(
                request.getCompany()
        );


        result.setRole(
                request.getRole()
        );


        result.setOptimizedSummary(
                optimizedSummary
        );


        result.setVerifiedExperienceBullets(
                verifiedExperience
        );


        result.setRejectedExperienceBullets(
                rejectedExperience
        );


        result.setVerifiedProjectBullets(
                verifiedProjects
        );


        result.setRejectedProjectBullets(
                rejectedProjects
        );


        result.setSkillsToHighlight(
                skillsToHighlight
        );


        result.setMissingSkillsNotAdded(
                missingSkills
        );


        result.setChanges(
                safeList(
                        candidate.getChanges()
                )
        );


        result.setStatus(
                buildStatus(
                        rejectedExperience,
                        rejectedProjects
                )
        );


        return result;
    }


    // =========================================================
    // JOB DESCRIPTION
    // =========================================================

    private JobDescriptionAnalysis
            analyzeJobDescription(
                    ResumeOptimizationRequest request) {

        JobDescriptionRequest jobRequest =
                new JobDescriptionRequest();


        jobRequest.setCompany(
                request.getCompany()
        );


        jobRequest.setRole(
                request.getRole()
        );


        jobRequest.setJobDescription(
                request.getJobDescription()
        );


        return jobDescriptionAnalyzer
                .analyze(
                        jobRequest
                );
    }


    // =========================================================
    // COLLECT REQUIRED + PREFERRED SKILLS
    // =========================================================

    private List<String> collectJobSkills(
            JobDescriptionAnalysis analysis) {

        Set<String> skills =
                new LinkedHashSet<>();


        if (analysis == null) {

            return new ArrayList<>();
        }


        if (analysis.getRequiredSkills()
                != null) {

            for (String skill :
                    analysis.getRequiredSkills()) {

                addCanonicalSkill(
                        skills,
                        skill
                );
            }
        }


        if (analysis.getPreferredSkills()
                != null) {

            for (String skill :
                    analysis.getPreferredSkills()) {

                addCanonicalSkill(
                        skills,
                        skill
                );
            }
        }


        return new ArrayList<>(
                skills
        );
    }


    private void addCanonicalSkill(
            Set<String> result,
            String skill) {

        if (skill == null
                || skill.isBlank()) {

            return;
        }


        String canonical =
                SkillCatalog
                        .resolveCanonicalSkill(
                                skill
                        );


        if (canonical != null) {

            result.add(
                    canonical
            );

        } else {

            result.add(
                    skill.trim()
            );
        }
    }


    // =========================================================
    // MATCHED SKILLS
    // =========================================================

    private List<String> findMatchedSkills(
            List<String> jobSkills,
            String resumeText) {

        Set<String> result =
                new LinkedHashSet<>();


        for (String skill :
                jobSkills) {

            if (SkillCatalog.containsSkill(
                    resumeText,
                    skill
            )) {

                String canonical =
                        SkillCatalog
                                .resolveCanonicalSkill(
                                        skill
                                );


                result.add(
                        canonical != null
                                ? canonical
                                : skill
                );
            }
        }


        return new ArrayList<>(
                result
        );
    }


    // =========================================================
    // MISSING SKILLS
    // =========================================================

    private List<String> findMissingSkills(
            List<String> jobSkills,
            String resumeText) {

        Set<String> result =
                new LinkedHashSet<>();


        for (String skill :
                jobSkills) {

            boolean exists =
                    SkillCatalog
                            .containsSkill(
                                    resumeText,
                                    skill
                            );


            if (!exists) {

                String canonical =
                        SkillCatalog
                                .resolveCanonicalSkill(
                                        skill
                                );


                result.add(
                        canonical != null
                                ? canonical
                                : skill
                );
            }
        }


        return new ArrayList<>(
                result
        );
    }


    // =========================================================
    // SUMMARY
    // =========================================================

    private String verifySummary(
            String candidateSummary,
            String resumeText) {

        if (candidateSummary == null
                || candidateSummary.isBlank()) {

            return "";
        }


        boolean supported =
                hallucinationGuard
                        .isClaimSupported(
                                candidateSummary,
                                resumeText
                        );


        if (!supported) {

            return "";
        }


        return candidateSummary
                .trim();
    }


    // =========================================================
    // STATUS
    // =========================================================

    private String buildStatus(
            List<String> rejectedExperience,
            List<String> rejectedProjects) {

        int rejected =
                safeList(
                        rejectedExperience
                ).size()
                +
                safeList(
                        rejectedProjects
                ).size();


        if (rejected == 0) {

            return "Optimization completed successfully. "
                    + "Role-relevant skills were matched "
                    + "deterministically against the original resume.";
        }


        return "Optimization completed. "
                + rejected
                + " unsupported AI suggestion(s) "
                + "were rejected.";
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validate(
            ResumeOptimizationRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Optimization request is required"
            );
        }


        if (request.getResumeId() == null) {

            throw new IllegalArgumentException(
                    "Resume id is required"
            );
        }


        if (request.getJobDescription()
                == null
                ||
                request
                        .getJobDescription()
                        .isBlank()) {

            throw new IllegalArgumentException(
                    "Job description is required"
            );
        }
    }


    private <T> List<T> safeList(
            List<T> value) {

        if (value == null) {

            return new ArrayList<>();
        }


        return value;
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}