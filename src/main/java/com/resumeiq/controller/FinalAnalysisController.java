package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.dto.FinalAnalysisRequest;
import com.resumeiq.dto.FinalAnalysisResult;
import com.resumeiq.service.FinalAnalysisService;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(
        origins = "http://localhost:5173"
)
public class FinalAnalysisController {

    private final FinalAnalysisService
            finalAnalysisService;


    public FinalAnalysisController(
            FinalAnalysisService
                    finalAnalysisService) {

        this.finalAnalysisService =
                finalAnalysisService;
    }


    @PostMapping("/final")
    public ResponseEntity<FinalAnalysisResult>
            analyze(
                    @RequestBody
                    FinalAnalysisRequest request) {

        FinalAnalysisResult result =
                finalAnalysisService
                        .analyze(request);


        return ResponseEntity.ok(result);
    }
}