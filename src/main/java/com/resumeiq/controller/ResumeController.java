package com.resumeiq.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.resumeiq.dto.ParsedResume;
import com.resumeiq.entity.Resume;
import com.resumeiq.service.ResumeService;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeController {

    private final ResumeService
            resumeService;


    public ResumeController(
            ResumeService resumeService) {

        this.resumeService =
                resumeService;
    }


    @PostMapping("/upload")
    public ResponseEntity<Resume>
            uploadResume(
                    @RequestParam("file")
                    MultipartFile file)
                    throws Exception {

        return ResponseEntity.ok(
                resumeService
                        .uploadResume(file)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Resume>
            getResume(
                    @PathVariable
                    Long id) {

        return ResponseEntity.ok(
                resumeService
                        .getResume(id)
        );
    }


    @GetMapping
    public ResponseEntity<List<Resume>>
            getAllResumes() {

        return ResponseEntity.ok(
                resumeService
                        .getAllResumes()
        );
    }


    @GetMapping("/{id}/parse")
    public ResponseEntity<ParsedResume>
            parseResume(
                    @PathVariable
                    Long id) {

        return ResponseEntity.ok(
                resumeService
                        .parseResume(id)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteResume(
                    @PathVariable
                    Long id) {

        resumeService.deleteResume(
                id
        );


        return ResponseEntity
                .noContent()
                .build();
    }
}