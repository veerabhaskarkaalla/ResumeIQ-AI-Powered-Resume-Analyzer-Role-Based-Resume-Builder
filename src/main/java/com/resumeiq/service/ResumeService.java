package com.resumeiq.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.resumeiq.document.DocumentTextExtractor;
import com.resumeiq.document.ResumeParser;
import com.resumeiq.dto.ParsedResume;
import com.resumeiq.entity.Resume;
import com.resumeiq.entity.User;
import com.resumeiq.repository.AtsAnalysisHistoryRepository;
import com.resumeiq.repository.OptimizationHistoryRepository;
import com.resumeiq.repository.ResumeRepository;
import com.resumeiq.repository.ResumeVersionRepository;
import com.resumeiq.security.CurrentUserService;

import jakarta.transaction.Transactional;

@Service
public class ResumeService {

    private static final long MAX_FILE_SIZE =
            10L * 1024L * 1024L;


    private final ResumeRepository
            resumeRepository;

    private final DocumentTextExtractor
            textExtractor;

    private final ResumeParser
            resumeParser;

    private final CurrentUserService
            currentUserService;

    private final ResumeVersionRepository
            versionRepository;

    private final OptimizationHistoryRepository
            optimizationHistoryRepository;

    private final AtsAnalysisHistoryRepository
            atsAnalysisHistoryRepository;


    public ResumeService(
            ResumeRepository resumeRepository,
            DocumentTextExtractor textExtractor,
            ResumeParser resumeParser,
            CurrentUserService currentUserService,
            ResumeVersionRepository versionRepository,
            OptimizationHistoryRepository optimizationHistoryRepository,
            AtsAnalysisHistoryRepository atsAnalysisHistoryRepository) {

        this.resumeRepository =
                resumeRepository;

        this.textExtractor =
                textExtractor;

        this.resumeParser =
                resumeParser;

        this.currentUserService =
                currentUserService;

        this.versionRepository =
                versionRepository;

        this.optimizationHistoryRepository =
                optimizationHistoryRepository;

        this.atsAnalysisHistoryRepository =
                atsAnalysisHistoryRepository;
    }


    public Resume uploadResume(
            MultipartFile file)
            throws Exception {

        validateFile(file);


        User currentUser =
                currentUserService
                        .getCurrentUser();


        String extractedText =
                textExtractor.extract(
                        file
                );


        if (extractedText == null
                || extractedText.isBlank()) {

            throw new IllegalArgumentException(
                    "No readable text found in the resume"
            );
        }


        Resume resume =
                new Resume();


        resume.setUserId(
                currentUser.getId()
        );


        resume.setFileName(
                sanitizeFileName(
                        file.getOriginalFilename()
                )
        );


        resume.setFileType(
                file.getContentType()
        );


        resume.setExtractedText(
                extractedText.trim()
        );


        resume.setUploadedAt(
                LocalDateTime.now()
        );


        return resumeRepository.save(
                resume
        );
    }


    public Resume getResume(
            Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "Resume id is required"
            );
        }


        Long currentUserId =
                currentUserService
                        .getCurrentUserId();


        return resumeRepository
                .findByIdAndUserId(
                        id,
                        currentUserId
                )
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Resume not found"
                                )
                );
    }


    public List<Resume> getAllResumes() {

        Long currentUserId =
                currentUserService
                        .getCurrentUserId();


        return resumeRepository
                .findByUserIdOrderByUploadedAtDesc(
                        currentUserId
                );
    }


    public ParsedResume parseResume(
            Long id) {

        Resume resume =
                getResume(id);


        return resumeParser.parse(
                resume.getExtractedText()
        );
    }


    @Transactional
    public void deleteResume(
            Long resumeId) {

        /*
         * First security check.
         * User can only delete their own resume.
         */
        Resume resume =
                getResume(
                        resumeId
                );


        /*
         * Delete child/history data first.
         */

        atsAnalysisHistoryRepository
                .deleteByResumeId(
                        resumeId
                );


        optimizationHistoryRepository
                .deleteByResumeId(
                        resumeId
                );


        versionRepository
                .deleteByResumeId(
                        resumeId
                );


        /*
         * Finally delete parent resume.
         */
        resumeRepository.delete(
                resume
        );
    }


    private void validateFile(
            MultipartFile file) {

        if (file == null
                || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Resume file cannot be empty"
            );
        }


        if (file.getSize()
                > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Resume file must be 10 MB or smaller"
            );
        }


        String fileName =
                file.getOriginalFilename();


        if (fileName == null
                || fileName.isBlank()) {

            throw new IllegalArgumentException(
                    "Resume filename is invalid"
            );
        }


        String lowerName =
                fileName
                        .toLowerCase(
                                Locale.ROOT
                        );


        boolean validExtension =
                lowerName.endsWith(".pdf")
                ||
                lowerName.endsWith(".docx");


        if (!validExtension) {

            throw new IllegalArgumentException(
                    "Only PDF and DOCX resumes are supported"
            );
        }
    }


    private String sanitizeFileName(
            String fileName) {

        if (fileName == null) {

            return "resume";
        }


        /*
         * Prevent fake paths such as:
         *
         * C:\Users\abc\resume.pdf
         * ../../resume.pdf
         */

        String cleaned =
                fileName
                        .replace("\\", "/");


        int lastSlash =
                cleaned.lastIndexOf("/");


        if (lastSlash >= 0) {

            cleaned =
                    cleaned.substring(
                            lastSlash + 1
                    );
        }


        cleaned =
                cleaned.replaceAll(
                        "[\\r\\n]",
                        ""
                );


        if (cleaned.isBlank()) {

            return "resume";
        }


        return cleaned;
    }
}