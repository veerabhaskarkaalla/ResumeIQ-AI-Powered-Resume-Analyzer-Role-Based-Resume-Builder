package com.resumeiq.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.resumeiq.ats.JobDescriptionAnalyzer;
import com.resumeiq.document.StructuredResumeParser;
import com.resumeiq.dto.AtsAnalysisRequest;
import com.resumeiq.dto.AtsAnalysisResult;
import com.resumeiq.dto.JobDescriptionAnalysis;
import com.resumeiq.dto.JobDescriptionRequest;
import com.resumeiq.dto.OptimizationComparisonResult;
import com.resumeiq.dto.ProtectedOptimizationResult;
import com.resumeiq.dto.ResumeOptimizationRequest;
import com.resumeiq.dto.ResumeOptimizationResult;
import com.resumeiq.dto.StructuredResume;
import com.resumeiq.entity.OptimizationHistory;
import com.resumeiq.entity.Resume;
import com.resumeiq.entity.ResumeVersion;
import com.resumeiq.repository.OptimizationHistoryRepository;
import com.resumeiq.repository.ResumeVersionRepository;

@Service
public class OptimizationScoringService {

    private final ResumeService resumeService;

    private final ResumeOptimizerService
            resumeOptimizerService;

    private final AtsService atsService;

    private final StructuredResumeParser
            structuredResumeParser;

    private final JobDescriptionAnalyzer
            jobDescriptionAnalyzer;

    private final ResumeVersionRepository
            versionRepository;

    private final OptimizationHistoryRepository
            historyRepository;

    private final StructuredResumeOptimizationService
            structuredOptimizationService;

    private final StructuredResumeJsonService
            jsonService;

    private final AtsScoreProtectionService
            scoreProtectionService;


    public OptimizationScoringService(
            ResumeService resumeService,
            ResumeOptimizerService resumeOptimizerService,
            AtsService atsService,
            StructuredResumeParser structuredResumeParser,
            JobDescriptionAnalyzer jobDescriptionAnalyzer,
            ResumeVersionRepository versionRepository,
            OptimizationHistoryRepository historyRepository,
            StructuredResumeOptimizationService structuredOptimizationService,
            StructuredResumeJsonService jsonService,
            AtsScoreProtectionService scoreProtectionService) {

        this.resumeService =
                resumeService;

        this.resumeOptimizerService =
                resumeOptimizerService;

        this.atsService =
                atsService;

        this.structuredResumeParser =
                structuredResumeParser;

        this.jobDescriptionAnalyzer =
                jobDescriptionAnalyzer;

        this.versionRepository =
                versionRepository;

        this.historyRepository =
                historyRepository;

        this.structuredOptimizationService =
                structuredOptimizationService;

        this.jsonService =
                jsonService;

        this.scoreProtectionService =
                scoreProtectionService;
    }


    public OptimizationComparisonResult
            optimizeAndCompare(
                    ResumeOptimizationRequest request) {

        validate(
                request
        );


        // =====================================================
        // ORIGINAL RESUME
        // =====================================================

        Resume originalResume =
                resumeService.getResume(
                        request.getResumeId()
                );


        String originalText =
                originalResume
                        .getExtractedText();


        if (originalText == null
                ||
            originalText.isBlank()) {

            throw new IllegalArgumentException(
                    "Resume contains no extracted text"
            );
        }


        // =====================================================
        // BEFORE ATS
        // =====================================================

        AtsAnalysisRequest beforeRequest =
                new AtsAnalysisRequest();


        beforeRequest.setResumeId(
                request.getResumeId()
        );


        beforeRequest.setCompany(
                request.getCompany()
        );


        beforeRequest.setRole(
                request.getRole()
        );


        beforeRequest.setJobDescription(
                request.getJobDescription()
        );


        AtsAnalysisResult before =
                atsService.analyze(
                        beforeRequest
                );


        // =====================================================
        // JD ANALYSIS
        // =====================================================

        JobDescriptionRequest jdRequest =
                new JobDescriptionRequest();


        jdRequest.setCompany(
                request.getCompany()
        );


        jdRequest.setRole(
                request.getRole()
        );


        jdRequest.setJobDescription(
                request.getJobDescription()
        );


        JobDescriptionAnalysis jobAnalysis =
                jobDescriptionAnalyzer
                        .analyze(
                                jdRequest
                        );


        // =====================================================
        // BASE OPTIMIZATION
        // =====================================================

        ResumeOptimizationResult baseOptimization =
                resumeOptimizerService
                        .optimize(
                                request
                        );


        // =====================================================
        // STRUCTURED CANDIDATE
        // =====================================================

        StructuredResume candidate =
                structuredOptimizationService
                        .optimize(
                                originalText,
                                request,
                                baseOptimization
                        );


        // =====================================================
        // SCORE PROTECTION
        // =====================================================

        ProtectedOptimizationResult protectedResult =
                scoreProtectionService
                        .protect(
                                request.getResumeId(),
                                originalText,
                                candidate,
                                jobAnalysis,
                                before
                        );


        StructuredResume finalStructured =
                protectedResult
                        .getStructuredResume();


        String finalText =
                protectedResult
                        .getResumeText();


        AtsAnalysisResult after =
                protectedResult
                        .getAtsResult();


        int improvement =
                after.getOverallScore()
                -
                before.getOverallScore();


        // =====================================================
        // HARD SCORE PROTECTION
        // =====================================================

        if (improvement < 0) {

            finalStructured =
                    structuredResumeParser
                            .parse(
                                    originalText
                            );


            finalText =
                    originalText;


            after =
                    before;


            improvement =
                    0;
        }


        // =====================================================
        // ORIGINAL VERSION
        // =====================================================

        ensureOriginalVersionExists(
                originalResume,
                before,
                request
        );


        int nextVersion =
                getNextVersionNumber(
                        request.getResumeId()
                );


        // =====================================================
        // SAVE OPTIMIZED VERSION
        // =====================================================

        ResumeVersion optimizedVersion =
                new ResumeVersion();


        optimizedVersion.setResumeId(
                request.getResumeId()
        );


        optimizedVersion.setVersionNumber(
                nextVersion
        );


        optimizedVersion.setVersionType(
                "OPTIMIZED"
        );


        optimizedVersion.setCompany(
                request.getCompany()
        );


        optimizedVersion.setRole(
                request.getRole()
        );


        optimizedVersion.setAtsScore(
                after.getOverallScore()
        );


        optimizedVersion.setContent(
                finalText
        );


        optimizedVersion.setStructuredContent(
                jsonService.toJson(
                        finalStructured
                )
        );


        optimizedVersion.setCreatedAt(
                LocalDateTime.now()
        );


        ResumeVersion savedVersion =
                versionRepository.save(
                        optimizedVersion
                );


        // =====================================================
        // HISTORY
        // =====================================================

        OptimizationHistory history =
                new OptimizationHistory();


        history.setResumeId(
                request.getResumeId()
        );


        history.setOptimizedVersionId(
                savedVersion.getId()
        );


        history.setCompany(
                request.getCompany()
        );


        history.setRole(
                request.getRole()
        );


        history.setBeforeScore(
                before.getOverallScore()
        );


        history.setAfterScore(
                after.getOverallScore()
        );


        history.setImprovement(
                improvement
        );


        history.setCreatedAt(
                LocalDateTime.now()
        );


        historyRepository.save(
                history
        );


        // =====================================================
        // RESPONSE
        // =====================================================

        OptimizationComparisonResult result =
                new OptimizationComparisonResult();


        result.setResumeId(
                request.getResumeId()
        );


        result.setOptimizedVersionId(
                savedVersion.getId()
        );


        result.setVersionNumber(
                nextVersion
        );


        result.setCompany(
                request.getCompany()
        );


        result.setRole(
                request.getRole()
        );


        // =====================================================
        // OVERALL
        // =====================================================

        result.setBeforeScore(
                before.getOverallScore()
        );


        result.setAfterScore(
                after.getOverallScore()
        );


        result.setImprovement(
                improvement
        );


        // =====================================================
        // KEYWORDS
        // =====================================================

        result.setBeforeKeywordMatch(
                before.getKeywordMatch()
        );


        result.setAfterKeywordMatch(
                after.getKeywordMatch()
        );


        // =====================================================
        // SKILLS
        // =====================================================

        result.setBeforeSkillsMatch(
                before.getSkillsMatch()
        );


        result.setAfterSkillsMatch(
                after.getSkillsMatch()
        );


        // =====================================================
        // EXPERIENCE
        // =====================================================

        result.setBeforeExperienceRelevance(
                before.getExperienceRelevance()
        );


        result.setAfterExperienceRelevance(
                after.getExperienceRelevance()
        );


        // =====================================================
        // PROJECTS
        // =====================================================

        result.setBeforeProjectsRelevance(
                before.getProjectsRelevance()
        );


        result.setAfterProjectsRelevance(
                after.getProjectsRelevance()
        );


        // =====================================================
        // EDUCATION
        // =====================================================

        result.setBeforeEducationFit(
                before.getEducationFit()
        );


        result.setAfterEducationFit(
                after.getEducationFit()
        );


        // =====================================================
        // STRUCTURE
        // =====================================================

        result.setBeforeStructure(
                before.getStructure()
        );


        result.setAfterStructure(
                after.getStructure()
        );


        // =====================================================
        // FORMATTING
        // =====================================================

        result.setBeforeFormatting(
                before.getFormatting()
        );


        result.setAfterFormatting(
                after.getFormatting()
        );


        // =====================================================
        // QUANTIFICATION
        // =====================================================

        result.setBeforeQuantification(
                before.getQuantification()
        );


        result.setAfterQuantification(
                after.getQuantification()
        );


        // =====================================================
        // OPTIMIZED TEXT
        // =====================================================

        result.setOptimizedResumeText(
                finalText
        );


        result.setSkillsToHighlight(
                safeList(
                        baseOptimization
                            .getSkillsToHighlight()
                )
        );


        result.setMissingSkillsNotAdded(
                safeList(
                        baseOptimization
                            .getMissingSkillsNotAdded()
                )
        );


        result.setRejectedExperienceBullets(
                safeList(
                        baseOptimization
                            .getRejectedExperienceBullets()
                )
        );


        result.setRejectedProjectBullets(
                safeList(
                        baseOptimization
                            .getRejectedProjectBullets()
                )
        );


        result.setStatus(
                buildStatus(
                        before,
                        after,
                        improvement,
                        protectedResult
                )
        );


        return result;
    }


    // =========================================================
    // STATUS
    // =========================================================

    private String buildStatus(
            AtsAnalysisResult before,
            AtsAnalysisResult after,
            int improvement,
            ProtectedOptimizationResult protectedResult) {

        String protection =
                protectedResult == null
                        ? ""
                        : safe(
                            protectedResult
                                .getProtectionMessage()
                        );


        if (improvement > 0) {

            return "Optimization completed successfully. "
                    + "ATS score improved from "
                    + before.getOverallScore()
                    + " to "
                    + after.getOverallScore()
                    + ". "
                    + protection;
        }


        if (protectedResult != null
                &&
            protectedResult
                .isRollbackApplied()) {

            return "Optimization completed with score protection. "
                    + "Original ATS score "
                    + before.getOverallScore()
                    + " was protected. "
                    + protection;
        }


        return "Optimization completed. "
                + "ATS score remained at "
                + after.getOverallScore()
                + ".";
    }


    // =========================================================
    // ORIGINAL VERSION
    // =========================================================

    private void ensureOriginalVersionExists(
            Resume resume,
            AtsAnalysisResult before,
            ResumeOptimizationRequest request) {

        List<ResumeVersion> existing =
                versionRepository
                        .findByResumeIdOrderByVersionNumberAsc(
                                resume.getId()
                        );


        if (!existing.isEmpty()) {

            return;
        }


        StructuredResume originalStructured =
                structuredResumeParser
                        .parse(
                                resume
                                    .getExtractedText()
                        );


        ResumeVersion originalVersion =
                new ResumeVersion();


        originalVersion.setResumeId(
                resume.getId()
        );


        originalVersion.setVersionNumber(
                1
        );


        originalVersion.setVersionType(
                "ORIGINAL"
        );


        originalVersion.setCompany(
                request.getCompany()
        );


        originalVersion.setRole(
                request.getRole()
        );


        originalVersion.setAtsScore(
                before.getOverallScore()
        );


        originalVersion.setContent(
                resume.getExtractedText()
        );


        originalVersion.setStructuredContent(
                jsonService.toJson(
                        originalStructured
                )
        );


        originalVersion.setCreatedAt(
                LocalDateTime.now()
        );


        versionRepository.save(
                originalVersion
        );
    }


    // =========================================================
    // VERSION NUMBER
    // =========================================================

    private int getNextVersionNumber(
            Long resumeId) {

        return versionRepository
                .findTopByResumeIdOrderByVersionNumberDesc(
                        resumeId
                )
                .map(
                        version ->
                                version
                                    .getVersionNumber()
                                    + 1
                )
                .orElse(
                        1
                );
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


        if (safe(
                request.getCompany()
        ).isBlank()) {

            throw new IllegalArgumentException(
                    "Company is required"
            );
        }


        if (safe(
                request.getRole()
        ).isBlank()) {

            throw new IllegalArgumentException(
                    "Target role is required"
            );
        }


        if (safe(
                request.getJobDescription()
        ).isBlank()) {

            throw new IllegalArgumentException(
                    "Job description is required"
            );
        }
    }


    private <T> List<T> safeList(
            List<T> value) {

        return value == null
                ? List.of()
                : value;
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}