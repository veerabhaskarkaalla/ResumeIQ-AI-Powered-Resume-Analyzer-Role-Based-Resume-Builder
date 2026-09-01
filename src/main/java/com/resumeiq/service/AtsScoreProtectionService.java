package com.resumeiq.service;

import org.springframework.stereotype.Service;

import com.resumeiq.ats.AtsEngine;
import com.resumeiq.document.ResumeParser;
import com.resumeiq.document.StructuredResumeParser;
import com.resumeiq.dto.AtsAnalysisResult;
import com.resumeiq.dto.JobDescriptionAnalysis;
import com.resumeiq.dto.ParsedResume;
import com.resumeiq.dto.ProtectedOptimizationResult;
import com.resumeiq.dto.StructuredResume;

@Service
public class AtsScoreProtectionService {

    private final ResumeParser resumeParser;

    private final StructuredResumeParser
            structuredResumeParser;

    private final ResumeTemplateService
            templateService;

    private final AtsEngine atsEngine;


    public AtsScoreProtectionService(
            ResumeParser resumeParser,
            StructuredResumeParser structuredResumeParser,
            ResumeTemplateService templateService,
            AtsEngine atsEngine) {

        this.resumeParser =
                resumeParser;

        this.structuredResumeParser =
                structuredResumeParser;

        this.templateService =
                templateService;

        this.atsEngine =
                atsEngine;
    }


    public ProtectedOptimizationResult protect(
            Long resumeId,
            String originalResumeText,
            StructuredResume optimizedCandidate,
            JobDescriptionAnalysis jobAnalysis,
            AtsAnalysisResult originalAtsResult) {

        if (optimizedCandidate == null) {

            return fallbackToOriginal(
                    originalResumeText,
                    originalAtsResult,
                    "Optimization candidate was empty."
            );
        }


        int baselineScore =
                originalAtsResult
                        .getOverallScore();


        StructuredResume original =
                structuredResumeParser.parse(
                        originalResumeText
                );


        // =====================================================
        // 1. FULL OPTIMIZED VERSION
        // =====================================================

        ProtectedOptimizationResult full =
                evaluateStructured(
                        resumeId,
                        optimizedCandidate,
                        jobAnalysis,
                        false,
                        "Full optimized resume accepted."
                );


        if (score(full)
                >= baselineScore) {

            return full;
        }


        ProtectedOptimizationResult best =
                full;


        // =====================================================
        // 2. ROLLBACK ONLY OBJECTIVE
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        false,
                                        true,
                                        true,
                                        true
                                ),
                                jobAnalysis,
                                true,
                                "Career objective rollback applied."
                        )
                );


        // =====================================================
        // 3. ROLLBACK ONLY EXPERIENCE
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        true,
                                        false,
                                        true,
                                        true
                                ),
                                jobAnalysis,
                                true,
                                "Experience optimization rollback applied."
                        )
                );


        // =====================================================
        // 4. ROLLBACK ONLY PROJECTS
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        true,
                                        true,
                                        false,
                                        true
                                ),
                                jobAnalysis,
                                true,
                                "Project optimization rollback applied."
                        )
                );


        // =====================================================
        // 5. ROLLBACK SKILL REORDER
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        true,
                                        true,
                                        true,
                                        false
                                ),
                                jobAnalysis,
                                true,
                                "Skill reordering rollback applied."
                        )
                );


        if (score(best)
                >= baselineScore) {

            return best;
        }


        // =====================================================
        // 6. OBJECTIVE + EXPERIENCE ROLLBACK
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        false,
                                        false,
                                        true,
                                        true
                                ),
                                jobAnalysis,
                                true,
                                "Career objective and experience rollback applied."
                        )
                );


        // =====================================================
        // 7. OBJECTIVE + PROJECT ROLLBACK
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        false,
                                        true,
                                        false,
                                        true
                                ),
                                jobAnalysis,
                                true,
                                "Career objective and project rollback applied."
                        )
                );


        // =====================================================
        // 8. EXPERIENCE + PROJECT ROLLBACK
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        true,
                                        false,
                                        false,
                                        true
                                ),
                                jobAnalysis,
                                true,
                                "Experience and project rollback applied."
                        )
                );


        if (score(best)
                >= baselineScore) {

            return best;
        }


        // =====================================================
        // 9. KEEP ONLY OPTIMIZED OBJECTIVE
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        true,
                                        false,
                                        false,
                                        false
                                ),
                                jobAnalysis,
                                true,
                                "Only the optimized career objective was retained."
                        )
                );


        // =====================================================
        // 10. KEEP ONLY OPTIMIZED EXPERIENCE
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        false,
                                        true,
                                        false,
                                        false
                                ),
                                jobAnalysis,
                                true,
                                "Only evidence-safe experience improvements were retained."
                        )
                );


        // =====================================================
        // 11. KEEP ONLY OPTIMIZED PROJECTS
        // =====================================================

        best =
                chooseBetter(
                        best,
                        evaluateStructured(
                                resumeId,
                                buildVariant(
                                        original,
                                        optimizedCandidate,
                                        false,
                                        false,
                                        true,
                                        false
                                ),
                                jobAnalysis,
                                true,
                                "Only evidence-safe project improvements were retained."
                        )
                );


        if (score(best)
                >= baselineScore) {

            return best;
        }


        // =====================================================
        // 12. ORIGINAL STRUCTURED VERSION
        // =====================================================

        ProtectedOptimizationResult
                structuredOriginal =
                evaluateStructured(
                        resumeId,
                        original,
                        jobAnalysis,
                        true,
                        "All score-reducing optimization changes were rolled back."
                );


        best =
                chooseBetter(
                        best,
                        structuredOriginal
                );


        if (score(best)
                >= baselineScore) {

            return best;
        }


        // =====================================================
        // 13. HARD SCORE PROTECTION
        //
        // Raw original was already scored by AtsService.
        // If template normalization itself changes score,
        // preserve original text and original ATS result.
        // =====================================================

        return fallbackToOriginal(
                originalResumeText,
                originalAtsResult,
                "Optimization was rolled back because every candidate scored below the original resume."
        );
    }


    // =========================================================
    // EVALUATE STRUCTURED VERSION
    // =========================================================

    private ProtectedOptimizationResult
            evaluateStructured(
                    Long resumeId,
                    StructuredResume structured,
                    JobDescriptionAnalysis jobAnalysis,
                    boolean rollbackApplied,
                    String message) {

        String text =
                templateService.render(
                        structured
                );


        ParsedResume parsed =
                resumeParser.parse(
                        text
                );


        AtsAnalysisResult ats =
                atsEngine.analyze(
                        resumeId,
                        text,
                        parsed,
                        jobAnalysis
                );


        ProtectedOptimizationResult result =
                new ProtectedOptimizationResult();


        result.setStructuredResume(
                structured
        );


        result.setResumeText(
                text
        );


        result.setAtsResult(
                ats
        );


        result.setRollbackApplied(
                rollbackApplied
        );


        result.setProtectionMessage(
                message
        );


        return result;
    }


    // =========================================================
    // BUILD VARIANT
    // =========================================================

    private StructuredResume buildVariant(
            StructuredResume original,
            StructuredResume optimized,
            boolean useOptimizedObjective,
            boolean useOptimizedExperience,
            boolean useOptimizedProjects,
            boolean useOptimizedSkills) {

        StructuredResume result =
                new StructuredResume();


        /*
         * Header is always original.
         */
        result.setName(
                original.getName()
        );


        result.setEmail(
                original.getEmail()
        );


        result.setPhone(
                original.getPhone()
        );


        result.setLocation(
                original.getLocation()
        );


        result.setLinks(
                original.getLinks()
        );


        // =====================================================
        // OBJECTIVE
        // =====================================================

        if (useOptimizedObjective) {

            result.setCareerObjective(
                    optimized.getCareerObjective()
            );

        } else {

            result.setCareerObjective(
                    original.getCareerObjective()
            );
        }


        // =====================================================
        // NEVER OPTIMIZED
        // =====================================================

        result.setEducation(
                original.getEducation()
        );


        result.setResearchPublications(
                original.getResearchPublications()
        );


        result.setCertifications(
                original.getCertifications()
        );


        result.setAchievements(
                original.getAchievements()
        );


        // =====================================================
        // EXPERIENCE
        // =====================================================

        if (useOptimizedExperience) {

            result.setExperience(
                    optimized.getExperience()
            );

        } else {

            result.setExperience(
                    original.getExperience()
            );
        }


        // =====================================================
        // PROJECTS
        // =====================================================

        if (useOptimizedProjects) {

            result.setProjects(
                    optimized.getProjects()
            );

        } else {

            result.setProjects(
                    original.getProjects()
            );
        }


        // =====================================================
        // SKILLS
        // =====================================================

        if (useOptimizedSkills) {

            result.setSkillCategories(
                    optimized.getSkillCategories()
            );

        } else {

            result.setSkillCategories(
                    original.getSkillCategories()
            );
        }


        return result;
    }


    // =========================================================
    // CHOOSE HIGHER SCORE
    // =========================================================

    private ProtectedOptimizationResult
            chooseBetter(
                    ProtectedOptimizationResult current,
                    ProtectedOptimizationResult candidate) {

        if (current == null) {

            return candidate;
        }


        if (candidate == null) {

            return current;
        }


        int currentScore =
                score(
                        current
                );


        int candidateScore =
                score(
                        candidate
                );


        if (candidateScore
                > currentScore) {

            return candidate;
        }


        /*
         * On equal score:
         *
         * Prefer version with fewer rollbacks.
         */
        if (candidateScore
                == currentScore) {

            if (current.isRollbackApplied()
                    &&
                !candidate.isRollbackApplied()) {

                return candidate;
            }
        }


        return current;
    }


    private int score(
            ProtectedOptimizationResult result) {

        if (result == null
                ||
            result.getAtsResult() == null) {

            return Integer.MIN_VALUE;
        }


        return result
                .getAtsResult()
                .getOverallScore();
    }


    // =========================================================
    // HARD FALLBACK
    // =========================================================

    private ProtectedOptimizationResult
            fallbackToOriginal(
                    String originalResumeText,
                    AtsAnalysisResult originalAts,
                    String message) {

        StructuredResume structured =
                structuredResumeParser.parse(
                        originalResumeText
                );


        ProtectedOptimizationResult result =
                new ProtectedOptimizationResult();


        result.setStructuredResume(
                structured
        );


        result.setResumeText(
                originalResumeText
        );


        result.setAtsResult(
                originalAts
        );


        result.setRollbackApplied(
                true
        );


        result.setProtectionMessage(
                message
        );


        return result;
    }
}