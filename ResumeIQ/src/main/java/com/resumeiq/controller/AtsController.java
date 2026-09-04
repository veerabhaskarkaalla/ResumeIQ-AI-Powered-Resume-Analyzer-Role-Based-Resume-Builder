package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.dto.AtsAnalysisRequest;
import com.resumeiq.dto.AtsAnalysisResult;
import com.resumeiq.service.AtsService;

@RestController
@RequestMapping("/api/ats")
@CrossOrigin(origins = "http://localhost:5173")
public class AtsController {

    private final AtsService atsService;

    public AtsController(
            AtsService atsService) {

        this.atsService =
                atsService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AtsAnalysisResult>
            analyze(
                    @RequestBody
                    AtsAnalysisRequest request) {

        return ResponseEntity.ok(
                atsService.analyze(request)
        );
    }
}