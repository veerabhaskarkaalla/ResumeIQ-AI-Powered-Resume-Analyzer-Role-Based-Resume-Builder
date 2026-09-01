package com.resumeiq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.entity.OptimizationHistory;

public interface OptimizationHistoryRepository
        extends JpaRepository<OptimizationHistory, Long> {

    List<OptimizationHistory>
            findByResumeIdOrderByCreatedAtDesc(
                    Long resumeId
            );


    void deleteByResumeId(
            Long resumeId
    );
}