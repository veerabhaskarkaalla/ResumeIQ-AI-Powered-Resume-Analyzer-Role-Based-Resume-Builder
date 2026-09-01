package com.resumeiq.ai;

import com.resumeiq.dto.AiOptimizationCandidate;
import com.resumeiq.dto.ResumeOptimizationRequest;
import com.resumeiq.dto.SemanticAnalysisRequest;
import com.resumeiq.dto.SemanticAnalysisResult;

public interface AiService {

    SemanticAnalysisResult analyzeResume(
            SemanticAnalysisRequest request
    );

    AiOptimizationCandidate generateOptimization(
            ResumeOptimizationRequest request
    );
}