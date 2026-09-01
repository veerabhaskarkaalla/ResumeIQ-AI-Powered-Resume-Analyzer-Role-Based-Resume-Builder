package com.resumeiq.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.resumeiq.ai.AiService;
import com.resumeiq.ai.HallucinationGuard;

import com.resumeiq.dto.AtsAnalysisRequest;
import com.resumeiq.dto.AtsAnalysisResult;
import com.resumeiq.dto.FinalAnalysisRequest;
import com.resumeiq.dto.FinalAnalysisResult;
import com.resumeiq.dto.SemanticAnalysisRequest;
import com.resumeiq.dto.SemanticAnalysisResult;

import com.resumeiq.entity.AtsAnalysisHistory;
import com.resumeiq.entity.Resume;

import com.resumeiq.repository.AtsAnalysisHistoryRepository;

@Service
public class FinalAnalysisService {

    private final AtsService atsService;

    private final AiService aiService;

    private final ResumeService resumeService;

    private final HallucinationGuard hallucinationGuard;

    private final AtsAnalysisHistoryRepository
            historyRepository;


    public FinalAnalysisService(
            AtsService atsService,
            AiService aiService,
            ResumeService resumeService,
            HallucinationGuard hallucinationGuard,
            AtsAnalysisHistoryRepository historyRepository) {

        this.atsService = atsService;

        this.aiService = aiService;

        this.resumeService = resumeService;

        this.hallucinationGuard =
                hallucinationGuard;

        this.historyRepository =
                historyRepository;
    }


    public FinalAnalysisResult analyze(
            FinalAnalysisRequest request) {

        validate(request);


        Resume resume =
                resumeService.getResume(
                        request.getResumeId()
                );


        AtsAnalysisRequest atsRequest =
                new AtsAnalysisRequest();

        atsRequest.setResumeId(
                request.getResumeId()
        );

        atsRequest.setCompany(
                request.getCompany()
        );

        atsRequest.setRole(
                request.getRole()
        );

        atsRequest.setJobDescription(
                request.getJobDescription()
        );


        AtsAnalysisResult atsResult =
                atsService.analyze(
                        atsRequest
                );


        SemanticAnalysisRequest aiRequest =
                new SemanticAnalysisRequest();

        aiRequest.setResumeId(
                request.getResumeId()
        );

        aiRequest.setCompany(
                request.getCompany()
        );

        aiRequest.setRole(
                request.getRole()
        );

        aiRequest.setJobDescription(
                request.getJobDescription()
        );


        SemanticAnalysisResult aiResult =
                aiService.analyzeResume(
                        aiRequest
                );


        List<String> verifiedStrengths =
                hallucinationGuard
                        .getVerifiedStrengths(
                                aiResult.getStrengths(),
                                resume.getExtractedText()
                        );


        List<String> rejectedStrengths =
                hallucinationGuard
                        .getRejectedStrengths(
                                aiResult.getStrengths(),
                                resume.getExtractedText()
                        );


        List<String> finalGaps =
                combineGaps(
                        atsResult.getMissingSkills(),
                        aiResult.getGaps()
                );


        int finalScore =
                calculateFinalScore(
                        atsResult.getOverallScore(),
                        aiResult.getSemanticMatchScore()
                );


        FinalAnalysisResult result =
                new FinalAnalysisResult();


        result.setResumeId(
                request.getResumeId()
        );

        result.setCompany(
                request.getCompany()
        );

        result.setRole(
                request.getRole()
        );


        result.setAtsScore(
                atsResult.getOverallScore()
        );


        result.setSemanticScore(
                aiResult.getSemanticMatchScore()
        );


        result.setFinalScore(
                finalScore
        );


        result.setKeywordMatch(
                atsResult.getKeywordMatch()
        );


        result.setSkillsMatch(
                atsResult.getSkillsMatch()
        );


        result.setExperienceRelevance(
                atsResult.getExperienceRelevance()
        );


        result.setProjectsRelevance(
                atsResult.getProjectsRelevance()
        );


        result.setStructure(
                atsResult.getStructure()
        );


        result.setFormatting(
                atsResult.getFormatting()
        );


        result.setQuantification(
                atsResult.getQuantification()
        );


        result.setMatchedSkills(
                atsResult.getMatchedSkills()
        );


        result.setMissingSkills(
                atsResult.getMissingSkills()
        );


        result.setVerifiedStrengths(
                verifiedStrengths
        );


        result.setRejectedStrengths(
                rejectedStrengths
        );


        result.setGaps(
                finalGaps
        );


        result.setSuggestions(
                safeList(
                        aiResult.getSuggestions()
                )
        );


        result.setAiSummary(
                aiResult.getSummary()
        );


        result.setRecommendation(
                buildRecommendation(
                        finalScore,
                        finalGaps
                )
        );


        saveAnalysisHistory(
                result
        );


        return result;
    }


    private void saveAnalysisHistory(
            FinalAnalysisResult result) {

        AtsAnalysisHistory history =
                new AtsAnalysisHistory();


        history.setResumeId(
                result.getResumeId()
        );


        history.setCompany(
                result.getCompany()
        );


        history.setRole(
                result.getRole()
        );


        history.setAtsScore(
                result.getAtsScore()
        );


        history.setSemanticScore(
                result.getSemanticScore()
        );


        history.setFinalScore(
                result.getFinalScore()
        );


        history.setKeywordMatch(
                result.getKeywordMatch()
        );


        history.setSkillsMatch(
                result.getSkillsMatch()
        );


        history.setCreatedAt(
                LocalDateTime.now()
        );


        historyRepository.save(
                history
        );
    }


    private int calculateFinalScore(
            int atsScore,
            int semanticScore) {

        double score =
                atsScore * 0.75
                +
                semanticScore * 0.25;


        return clamp(
                (int) Math.round(score)
        );
    }


    private List<String> combineGaps(
            List<String> missingSkills,
            List<String> aiGaps) {

        Set<String> gaps =
                new LinkedHashSet<>();


        if (missingSkills != null) {

            for (String skill :
                    missingSkills) {

                gaps.add(
                        "Missing skill: "
                        + skill
                );
            }
        }


        if (aiGaps != null) {

            gaps.addAll(
                    aiGaps
            );
        }


        return new ArrayList<>(
                gaps
        );
    }


    private List<String> safeList(
            List<String> values) {

        if (values == null) {

            return new ArrayList<>();
        }


        return values;
    }


    private String buildRecommendation(
            int finalScore,
            List<String> gaps) {

        if (finalScore >= 85) {

            return "Strong match for the target role. "
                    + "Focus on small wording and "
                    + "quantification improvements.";
        }


        if (finalScore >= 70) {

            return "Good match. Improve JD-specific "
                    + "keywords and strengthen evidence "
                    + "for the identified gaps.";
        }


        if (finalScore >= 50) {

            return "Moderate match. Improve relevant "
                    + "projects, skills evidence and "
                    + "job-description alignment.";
        }


        if (gaps != null
                && !gaps.isEmpty()) {

            return "Low match. Build genuine evidence "
                    + "for missing requirements before "
                    + "adding them to the resume.";
        }


        return "Resume requires significant "
                + "improvement for this role.";
    }


    private void validate(
            FinalAnalysisRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Request cannot be null"
            );
        }


        if (request.getResumeId() == null) {

            throw new IllegalArgumentException(
                    "Resume id is required"
            );
        }


        if (request.getJobDescription() == null
                || request
                    .getJobDescription()
                    .isBlank()) {

            throw new IllegalArgumentException(
                    "Job description is required"
            );
        }
    }


    private int clamp(int value) {

        return Math.max(
                0,
                Math.min(
                        100,
                        value
                )
        );
    }
}