package com.resumeiq.service;

import org.springframework.stereotype.Service;

import com.resumeiq.document.StructuredResumeParser;
import com.resumeiq.dto.StructuredResume;
import com.resumeiq.entity.ResumeVersion;
import com.resumeiq.repository.ResumeVersionRepository;

@Service
public class ResumeExportService {

    private final ResumeVersionRepository
            versionRepository;

    private final StructuredResumeJsonService
            jsonService;

    private final StructuredResumeParser
            structuredResumeParser;

    private final ProfessionalResumeRendererService
            rendererService;

    private final LatexResumeTemplateService
            latexTemplateService;

    private final LatexPdfCompilerService
            latexPdfCompilerService;


    public ResumeExportService(
            ResumeVersionRepository versionRepository,
            StructuredResumeJsonService jsonService,
            StructuredResumeParser structuredResumeParser,
            ProfessionalResumeRendererService rendererService,
            LatexResumeTemplateService latexTemplateService,
            LatexPdfCompilerService latexPdfCompilerService) {

        this.versionRepository =
                versionRepository;

        this.jsonService =
                jsonService;

        this.structuredResumeParser =
                structuredResumeParser;

        this.rendererService =
                rendererService;

        this.latexTemplateService =
                latexTemplateService;

        this.latexPdfCompilerService =
                latexPdfCompilerService;
    }


    // =========================================================
    // GET VERSION
    // =========================================================

    public ResumeVersion getVersion(
            Long versionId) {

        if (versionId == null) {

            throw new IllegalArgumentException(
                    "Version id is required"
            );
        }


        return versionRepository
                .findById(versionId)
                .orElseThrow(
                        () ->
                            new IllegalArgumentException(
                                    "Resume version not found: "
                                    + versionId
                            )
                );
    }


    // =========================================================
    // PDF
    // =========================================================

    public byte[] generatePdf(
            Long versionId) {

        ResumeVersion version =
                getVersion(
                        versionId
                );


        StructuredResume resume =
                resolveStructuredResume(
                        version
                );


        String latex =
                latexTemplateService
                        .render(
                                resume
                        );


        return latexPdfCompilerService
                .compile(
                        latex
                );
    }


    // =========================================================
    // DOCX
    // =========================================================

    public byte[] generateDocx(
            Long versionId) {

        ResumeVersion version =
                getVersion(
                        versionId
                );


        StructuredResume resume =
                resolveStructuredResume(
                        version
                );


        /*
         * DOCX still uses the Java renderer.
         * PDF now uses the approved LaTeX template.
         */
        return rendererService
                .renderDocx(
                        resume
                );
    }


    // =========================================================
    // STRUCTURED RESUME
    // =========================================================

    private StructuredResume
            resolveStructuredResume(
                    ResumeVersion version) {

        String structuredJson =
                version.getStructuredContent();


        if (structuredJson != null
                &&
            !structuredJson.isBlank()) {

            try {

                StructuredResume resume =
                        jsonService.fromJson(
                                structuredJson
                        );


                if (resume != null) {

                    return resume;
                }


            } catch (Exception ignored) {

                /*
                 * Older versions may contain invalid or old
                 * structured JSON. Fall back to stored text.
                 */
            }
        }


        String content =
                version.getContent();


        if (content == null
                ||
            content.isBlank()) {

            throw new IllegalArgumentException(
                    "Resume version has no content"
            );
        }


        return structuredResumeParser
                .parse(
                        content
                );
    }


    // =========================================================
    // FILE NAME
    // =========================================================

    public String buildFileName(
            ResumeVersion version,
            String extension) {

        String role =
                safe(
                    version.getRole()
                );


        if (role.isBlank()) {

            role = "Resume";
        }


        String cleanRole =
                role.replaceAll(
                        "[^a-zA-Z0-9._-]+",
                        "_"
                );


        String versionPart =
                version.getVersionNumber() == null
                        ? ""
                        : "_v"
                          + version.getVersionNumber();


        String ext =
                safe(extension)
                        .replace(".", "")
                        .trim();


        if (ext.isBlank()) {

            ext = "pdf";
        }


        return cleanRole
                + versionPart
                + "."
                + ext;
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}