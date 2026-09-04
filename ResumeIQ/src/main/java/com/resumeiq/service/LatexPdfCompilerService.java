package com.resumeiq.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class LatexPdfCompilerService {

    private static final long COMPILE_TIMEOUT_SECONDS = 180;


    public byte[] compile(String latexSource) {

        if (latexSource == null
                || latexSource.isBlank()) {

            throw new IllegalArgumentException(
                    "LaTeX source is required"
            );
        }


        Path tempDirectory = null;


        try {

            tempDirectory =
                    Files.createTempDirectory(
                            "resumeiq-latex-"
                    );


            Path texFile =
                    tempDirectory.resolve(
                            "resume.tex"
                    );


            Files.writeString(
                    texFile,
                    latexSource,
                    StandardCharsets.UTF_8
            );


            String pdflatex =
                    resolvePdflatexExecutable();


            // First pass
            runPdfLatex(
                    pdflatex,
                    tempDirectory,
                    1
            );


            // Second pass
            runPdfLatex(
                    pdflatex,
                    tempDirectory,
                    2
            );


            Path pdfFile =
                    tempDirectory.resolve(
                            "resume.pdf"
                    );


            if (!Files.isRegularFile(pdfFile)) {

                throw new RuntimeException(
                        "LaTeX compilation finished, "
                        + "but resume.pdf was not generated."
                );
            }


            return Files.readAllBytes(
                    pdfFile
            );


        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to generate LaTeX PDF: "
                    + e.getMessage(),
                    e
            );


        } finally {

            deleteDirectoryQuietly(
                    tempDirectory
            );
        }
    }


    // =========================================================
    // FIND PDFLATEX
    // =========================================================

    private String resolvePdflatexExecutable() {

        /*
         * Try system PATH first.
         */
        if (commandWorks("pdflatex")) {

            return "pdflatex";
        }


        List<Path> candidates =
                new ArrayList<>();


        // =====================================================
        // LOCAL APP DATA
        // =====================================================

        String localAppData =
                System.getenv("LOCALAPPDATA");


        if (localAppData != null
                && !localAppData.isBlank()) {

            candidates.add(
                    Paths.get(
                            localAppData,
                            "Programs",
                            "MiKTeX",
                            "miktex",
                            "bin",
                            "x64",
                            "pdflatex.exe"
                    )
            );


            candidates.add(
                    Paths.get(
                            localAppData,
                            "Programs",
                            "MiKTeX",
                            "miktex",
                            "bin",
                            "pdflatex.exe"
                    )
            );
        }


        // =====================================================
        // USER HOME
        // =====================================================

        String userHome =
                System.getProperty("user.home");


        if (userHome != null
                && !userHome.isBlank()) {

            candidates.add(
                    Paths.get(
                            userHome,
                            "AppData",
                            "Local",
                            "Programs",
                            "MiKTeX",
                            "miktex",
                            "bin",
                            "x64",
                            "pdflatex.exe"
                    )
            );


            candidates.add(
                    Paths.get(
                            userHome,
                            "AppData",
                            "Local",
                            "Programs",
                            "MiKTeX",
                            "miktex",
                            "bin",
                            "pdflatex.exe"
                    )
            );
        }


        // =====================================================
        // PROGRAM FILES
        // =====================================================

        String programFiles =
                System.getenv("ProgramFiles");


        if (programFiles != null
                && !programFiles.isBlank()) {

            candidates.add(
                    Paths.get(
                            programFiles,
                            "MiKTeX",
                            "miktex",
                            "bin",
                            "x64",
                            "pdflatex.exe"
                    )
            );


            candidates.add(
                    Paths.get(
                            programFiles,
                            "MiKTeX",
                            "miktex",
                            "bin",
                            "pdflatex.exe"
                    )
            );
        }


        // =====================================================
        // FIND VALID EXECUTABLE
        // =====================================================

        for (Path candidate : candidates) {

            if (Files.isRegularFile(candidate)) {

                return candidate
                        .toAbsolutePath()
                        .toString();
            }
        }


        throw new RuntimeException(
                "pdflatex was not found. "
                + "Install MiKTeX and verify "
                + "'pdflatex --version' works."
        );
    }


    // =========================================================
    // CHECK COMMAND
    // =========================================================

    private boolean commandWorks(
            String command) {

        try {

            ProcessBuilder builder =
                    new ProcessBuilder(
                            command,
                            "--version"
                    );


            /*
             * We do not need command output here.
             * Discarding it prevents process-buffer blocking.
             */
            builder.redirectOutput(
                    ProcessBuilder.Redirect.DISCARD
            );


            builder.redirectError(
                    ProcessBuilder.Redirect.DISCARD
            );


            Process process =
                    builder.start();


            boolean finished =
                    process.waitFor(
                            10,
                            TimeUnit.SECONDS
                    );


            if (!finished) {

                process.destroyForcibly();

                return false;
            }


            return process.exitValue() == 0;


        } catch (Exception e) {

            return false;
        }
    }


    // =========================================================
    // RUN PDFLATEX
    // =========================================================

    private void runPdfLatex(
            String pdflatexExecutable,
            Path workingDirectory,
            int passNumber) {

        Path compilerOutput =
                workingDirectory.resolve(
                        "pdflatex-pass-"
                        + passNumber
                        + ".txt"
                );


        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        pdflatexExecutable,

                        "--enable-installer",

                        "-interaction=nonstopmode",

                        "-halt-on-error",

                        "-file-line-error",

                        "resume.tex"
                );


        processBuilder.directory(
                workingDirectory.toFile()
        );


        processBuilder.redirectErrorStream(
                true
        );


        /*
         * Send LaTeX output directly to file.
         * Prevents Windows pipe-buffer deadlock.
         */
        processBuilder.redirectOutput(
                compilerOutput.toFile()
        );


        try {

            Process process =
                    processBuilder.start();


            boolean finished =
                    process.waitFor(
                            COMPILE_TIMEOUT_SECONDS,
                            TimeUnit.SECONDS
                    );


            if (!finished) {

                process.destroyForcibly();


                String output =
                        readFileQuietly(
                                compilerOutput
                        );


                throw new RuntimeException(
                        "LaTeX compilation timed out after "
                        + COMPILE_TIMEOUT_SECONDS
                        + " seconds.\n\n"
                        + extractUsefulError(output)
                );
            }


            int exitCode =
                    process.exitValue();


            String output =
                    readFileQuietly(
                            compilerOutput
                    );


            if (exitCode != 0) {

                throw new RuntimeException(
                        "LaTeX compilation failed.\n\n"
                        + extractUsefulError(output)
                );
            }


        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to start pdflatex from: "
                    + pdflatexExecutable,
                    e
            );


        } catch (InterruptedException e) {

            Thread.currentThread()
                    .interrupt();


            throw new RuntimeException(
                    "LaTeX compilation was interrupted.",
                    e
            );
        }
    }


    // =========================================================
    // READ LOG
    // =========================================================

    private String readFileQuietly(
            Path file) {

        if (file == null
                || !Files.exists(file)) {

            return "";
        }


        try {

            return Files.readString(
                    file,
                    StandardCharsets.UTF_8
            );


        } catch (IOException e) {

            return "";
        }
    }


    // =========================================================
    // ERROR EXTRACTION
    // =========================================================

    private String extractUsefulError(
            String output) {

        if (output == null
                || output.isBlank()) {

            return "No useful compiler output was returned.";
        }


        String[] lines =
                output.split("\\R");


        StringBuilder important =
                new StringBuilder();


        for (String line : lines) {

            String lower =
                    line.toLowerCase();


            if (line.startsWith("!")
                    || lower.contains("error")
                    || lower.contains("fatal")
                    || lower.contains(
                        "undefined control sequence"
                    )
                    || lower.contains(
                        "emergency stop"
                    )
                    || lower.contains(
                        "not found"
                    )
                    || lower.contains(
                        "missing"
                    )
                    || lower.contains(
                        "could not"
                    )) {

                important
                        .append(line)
                        .append("\n");
            }
        }


        if (!important
                .toString()
                .isBlank()) {

            return important.toString();
        }


        /*
         * No obvious error.
         * Return last 30 log lines.
         */
        int start =
                Math.max(
                        0,
                        lines.length - 30
                );


        StringBuilder tail =
                new StringBuilder();


        for (int i = start;
             i < lines.length;
             i++) {

            tail.append(lines[i])
                .append("\n");
        }


        return tail.toString();
    }


    // =========================================================
    // CLEANUP
    // =========================================================

    private void deleteDirectoryQuietly(
            Path directory) {

        if (directory == null
                || !Files.exists(directory)) {

            return;
        }


        try {

            try (var paths =
                    Files.walk(directory)) {

                paths
                    .sorted(
                            Comparator.reverseOrder()
                    )
                    .forEach(
                            path -> {

                                try {

                                    Files.deleteIfExists(
                                            path
                                    );

                                } catch (IOException ignored) {

                                }
                            }
                    );
            }


        } catch (IOException ignored) {

        }
    }
}