package com.resumeiq.service;

import org.springframework.stereotype.Service;

import com.resumeiq.ats.AtsEngine;
import com.resumeiq.ats.JobDescriptionAnalyzer;
import com.resumeiq.dto.AtsAnalysisRequest;
import com.resumeiq.dto.AtsAnalysisResult;
import com.resumeiq.dto.JobDescriptionAnalysis;
import com.resumeiq.dto.JobDescriptionRequest;
import com.resumeiq.dto.ParsedResume;
import com.resumeiq.entity.Resume;

@Service
public class AtsService {

    private final ResumeService resumeService;

    private final JobDescriptionAnalyzer
            jobAnalyzer;

    private final AtsEngine atsEngine;

    public AtsService(
            ResumeService resumeService,
            JobDescriptionAnalyzer jobAnalyzer,
            AtsEngine atsEngine) {

        this.resumeService =
                resumeService;

        this.jobAnalyzer =
                jobAnalyzer;

        this.atsEngine =
                atsEngine;
    }

    public AtsAnalysisResult analyze(
            AtsAnalysisRequest request) {

        if (request == null
                || request.getResumeId() == null) {

            throw new IllegalArgumentException(
                    "Resume id is required"
            );
        }

        if (request.getJobDescription() == null
                || request.getJobDescription()
                          .isBlank()) {

            throw new IllegalArgumentException(
                    "Job description is required"
            );
        }

        Resume resume =
                resumeService.getResume(
                        request.getResumeId()
                );

        ParsedResume parsedResume =
                resumeService.parseResume(
                        request.getResumeId()
                );

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

        JobDescriptionAnalysis
                jobAnalysis =
                jobAnalyzer.analyze(
                        jobRequest
                );

        return atsEngine.analyze(
                resume.getId(),
                resume.getExtractedText(),
                parsedResume,
                jobAnalysis
        );
    }
}