package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.ai.AiService;
import com.resumeiq.dto.SemanticAnalysisRequest;
import com.resumeiq.dto.SemanticAnalysisResult;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(
        origins = "http://localhost:5173"
)
public class AiController {

    private final AiService aiService;

    public AiController(
            AiService aiService) {

        this.aiService =
                aiService;
    }


    @PostMapping("/semantic-analyze")
    public ResponseEntity<SemanticAnalysisResult>
            semanticAnalyze(
                    @RequestBody
                    SemanticAnalysisRequest request) {

        SemanticAnalysisResult result =
                aiService.analyzeResume(
                        request
                );

        return ResponseEntity.ok(result);
    }
}