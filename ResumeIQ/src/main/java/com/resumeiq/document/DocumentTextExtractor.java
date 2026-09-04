package com.resumeiq.document;

import java.io.ByteArrayInputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocumentTextExtractor {

    public String extract(MultipartFile file) throws Exception {

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IllegalArgumentException("Invalid file");
        }

        String lowerName = fileName.toLowerCase();

        if (lowerName.endsWith(".pdf")) {
            return extractPdf(file);
        }

        if (lowerName.endsWith(".docx")) {
            return extractDocx(file);
        }

        throw new IllegalArgumentException(
                "Only PDF and DOCX files are supported"
        );
    }

    private String extractPdf(MultipartFile file) throws Exception {

        try (PDDocument document =
                     Loader.loadPDF(file.getBytes())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);
        }
    }

    private String extractDocx(MultipartFile file) throws Exception {

        try (XWPFDocument document =
                     new XWPFDocument(
                         new ByteArrayInputStream(file.getBytes())
                     )) {

            StringBuilder text = new StringBuilder();

            document.getParagraphs().forEach(paragraph ->
                text.append(paragraph.getText())
                    .append("\n")
            );

            return text.toString();
        }
    }
}