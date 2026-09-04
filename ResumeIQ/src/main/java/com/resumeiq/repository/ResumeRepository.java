package com.resumeiq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.entity.Resume;

public interface ResumeRepository
        extends JpaRepository<Resume, Long> {

    Optional<Resume> findByIdAndUserId(
            Long id,
            Long userId
    );


    List<Resume> findByUserIdOrderByUploadedAtDesc(
            Long userId
    );
}