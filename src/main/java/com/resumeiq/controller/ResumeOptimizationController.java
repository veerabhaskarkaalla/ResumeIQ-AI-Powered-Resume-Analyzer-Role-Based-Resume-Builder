package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.dto.OptimizationComparisonResult;
import com.resumeiq.dto.ResumeOptimizationRequest;
import com.resumeiq.dto.ResumeOptimizationResult;
import com.resumeiq.service.OptimizationScoringService;
import com.resumeiq.service.ResumeOptimizerService;

@RestController
@RequestMapping("/api/optimizer")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeOptimizationController {

    private final ResumeOptimizerService
            optimizerService;

    private final OptimizationScoringService
            scoringService;


    public ResumeOptimizationController(
            ResumeOptimizerService optimizerService,
            OptimizationScoringService scoringService) {

        this.optimizerService =
                optimizerService;

        this.scoringService =
                scoringService;
    }


    @PostMapping("/optimize")
    public ResponseEntity<ResumeOptimizationResult>
            optimize(
                    @RequestBody
                    ResumeOptimizationRequest request) {

        return ResponseEntity.ok(
                optimizerService.optimize(
                        request
                )
        );
    }


    @PostMapping("/optimize-and-score")
    public ResponseEntity<OptimizationComparisonResult>
            optimizeAndScore(
                    @RequestBody
                    ResumeOptimizationRequest request) {

        return ResponseEntity.ok(
                scoringService.optimizeAndCompare(
                        request
                )
        );
    }
}