package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.ats.JobDescriptionAnalyzer;
import com.resumeiq.dto.JobDescriptionAnalysis;
import com.resumeiq.dto.JobDescriptionRequest;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class JobDescriptionController {

    private final JobDescriptionAnalyzer
            analyzer;

    public JobDescriptionController(
            JobDescriptionAnalyzer analyzer) {

        this.analyzer = analyzer;
    }

    @PostMapping("/analyze")
    public ResponseEntity<JobDescriptionAnalysis>
            analyze(
                    @RequestBody
                    JobDescriptionRequest request) {

        return ResponseEntity.ok(
                analyzer.analyze(request)
        );
    }
}