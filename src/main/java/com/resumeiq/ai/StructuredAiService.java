package com.resumeiq.ai;

import com.resumeiq.dto.BulletOptimizationRequest;
import com.resumeiq.dto.BulletOptimizationResult;

public interface StructuredAiService {

    BulletOptimizationResult optimizeBullets(
            BulletOptimizationRequest request
    );
}