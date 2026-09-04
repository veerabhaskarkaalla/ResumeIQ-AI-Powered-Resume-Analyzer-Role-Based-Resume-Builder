package com.resumeiq.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import com.resumeiq.entity.ResumeVersion;

import com.resumeiq.repository.ResumeVersionRepository;

import com.resumeiq.service.ResumeExportService;
import com.resumeiq.service.ResumeService;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeExportController {

    private final ResumeExportService
            exportService;

    private final ResumeVersionRepository
            versionRepository;

    private final ResumeService
            resumeService;


    public ResumeExportController(
            ResumeExportService exportService,
            ResumeVersionRepository versionRepository,
            ResumeService resumeService) {

        this.exportService =
                exportService;

        this.versionRepository =
                versionRepository;

        this.resumeService =
                resumeService;
    }


    @GetMapping("/version/{versionId}/pdf")
    public ResponseEntity<byte[]>
            downloadPdf(
                    @PathVariable
                    Long versionId) {

        ResumeVersion version =
                getOwnedVersion(
                        versionId
                );


        byte[] pdf =
                exportService.generatePdf(
                        versionId
                );


        String fileName =
                exportService.buildFileName(
                        version,
                        "pdf"
                );


        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                        + fileName
                        + "\""
                )

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .body(pdf);
    }


    @GetMapping("/version/{versionId}/docx")
    public ResponseEntity<byte[]>
            downloadDocx(
                    @PathVariable
                    Long versionId) {

        ResumeVersion version =
                getOwnedVersion(
                        versionId
                );


        byte[] docx =
                exportService.generateDocx(
                        versionId
                );


        String fileName =
                exportService.buildFileName(
                        version,
                        "docx"
                );


        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                        + fileName
                        + "\""
                )

                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        )
                )

                .body(docx);
    }


    private ResumeVersion getOwnedVersion(
            Long versionId) {

        ResumeVersion version =
                versionRepository
                        .findById(
                                versionId
                        )
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Resume version not found"
                                        )
                        );


        /*
         * ResumeService itself checks whether
         * the parent resume belongs to the
         * currently logged-in user.
         */
        resumeService.getResume(
                version.getResumeId()
        );


        return version;
    }
}