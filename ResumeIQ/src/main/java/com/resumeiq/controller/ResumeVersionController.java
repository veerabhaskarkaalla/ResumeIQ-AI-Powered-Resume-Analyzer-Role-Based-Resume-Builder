package com.resumeiq.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.resumeiq.entity.OptimizationHistory;
import com.resumeiq.entity.ResumeVersion;

import com.resumeiq.repository.OptimizationHistoryRepository;
import com.resumeiq.repository.ResumeVersionRepository;

import com.resumeiq.service.ResumeService;

@RestController
@RequestMapping("/api/resume-history")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeVersionController {

    private final ResumeVersionRepository
            versionRepository;

    private final OptimizationHistoryRepository
            historyRepository;

    private final ResumeService
            resumeService;


    public ResumeVersionController(
            ResumeVersionRepository versionRepository,
            OptimizationHistoryRepository historyRepository,
            ResumeService resumeService) {

        this.versionRepository =
                versionRepository;

        this.historyRepository =
                historyRepository;

        this.resumeService =
                resumeService;
    }


    @GetMapping("/{resumeId}/versions")
    public ResponseEntity<List<ResumeVersion>>
            getVersions(
                    @PathVariable
                    Long resumeId) {

        /*
         * Security check.
         *
         * If this resume does not belong to the
         * logged-in user this method throws 404.
         */
        resumeService.getResume(
                resumeId
        );


        List<ResumeVersion> versions =
                versionRepository
                        .findByResumeIdOrderByVersionNumberAsc(
                                resumeId
                        );


        return ResponseEntity.ok(
                versions
        );
    }


    @GetMapping("/{resumeId}/optimizations")
    public ResponseEntity<List<OptimizationHistory>>
            getOptimizationHistory(
                    @PathVariable
                    Long resumeId) {

        resumeService.getResume(
                resumeId
        );


        List<OptimizationHistory> history =
                historyRepository
                        .findByResumeIdOrderByCreatedAtDesc(
                                resumeId
                        );


        return ResponseEntity.ok(
                history
        );
    }
}