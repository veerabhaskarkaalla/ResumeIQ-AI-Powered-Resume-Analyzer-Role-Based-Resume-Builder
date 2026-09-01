package com.resumeiq.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String fileName;

    private String fileType;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String extractedText;

    private LocalDateTime uploadedAt;


    public Resume() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileType() {
        return fileType;
    }


    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public String getExtractedText() {
        return extractedText;
    }


    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }


    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }


    public void setUploadedAt(
            LocalDateTime uploadedAt) {

        this.uploadedAt = uploadedAt;
    }
}