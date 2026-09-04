package com.resumeiq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.entity.ResumeVersion;

public interface ResumeVersionRepository
        extends JpaRepository<ResumeVersion, Long> {

    List<ResumeVersion>
            findByResumeIdOrderByVersionNumberAsc(
                    Long resumeId
            );


    Optional<ResumeVersion>
            findTopByResumeIdOrderByVersionNumberDesc(
                    Long resumeId
            );


    void deleteByResumeId(
            Long resumeId
    );
}