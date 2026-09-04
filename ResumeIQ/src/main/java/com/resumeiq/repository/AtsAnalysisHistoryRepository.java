package com.resumeiq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.entity.AtsAnalysisHistory;

public interface AtsAnalysisHistoryRepository
        extends JpaRepository<AtsAnalysisHistory, Long> {

    List<AtsAnalysisHistory>
            findByResumeIdOrderByCreatedAtDesc(
                    Long resumeId
            );


    void deleteByResumeId(
            Long resumeId
    );
}