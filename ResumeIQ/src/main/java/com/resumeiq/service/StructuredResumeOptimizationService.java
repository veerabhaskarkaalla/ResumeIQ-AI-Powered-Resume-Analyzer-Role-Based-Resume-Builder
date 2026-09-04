package com.resumeiq.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.resumeiq.ai.StructuredAiService;
import com.resumeiq.document.StructuredResumeParser;
import com.resumeiq.dto.BulletOptimizationRequest;
import com.resumeiq.dto.BulletOptimizationResult;
import com.resumeiq.dto.BulletRewrite;
import com.resumeiq.dto.ExperienceEntry;
import com.resumeiq.dto.ProjectEntry;
import com.resumeiq.dto.ResumeOptimizationRequest;
import com.resumeiq.dto.ResumeOptimizationResult;
import com.resumeiq.dto.SkillCategory;
import com.resumeiq.dto.StructuredResume;
import com.resumeiq.util.SkillCatalog;

@Service
public class StructuredResumeOptimizationService {

    private final StructuredResumeParser
            structuredResumeParser;

    private final StructuredAiService
            structuredAiService;

    private final FactPreservationValidator
            factPreservationValidator;

    private final BulletQualityScorer
            bulletQualityScorer;


    public StructuredResumeOptimizationService(
            StructuredResumeParser structuredResumeParser,
            StructuredAiService structuredAiService,
            FactPreservationValidator factPreservationValidator,
            BulletQualityScorer bulletQualityScorer) {

        this.structuredResumeParser =
                structuredResumeParser;

        this.structuredAiService =
                structuredAiService;

        this.factPreservationValidator =
                factPreservationValidator;

        this.bulletQualityScorer =
                bulletQualityScorer;
    }


    public StructuredResume optimize(
            String originalResumeText,
            ResumeOptimizationRequest request,
            ResumeOptimizationResult baseOptimization) {

        StructuredResume resume =
                structuredResumeParser.parse(
                        originalResumeText
                );


        /*
         * IMPORTANT:
         *
         * Education
         * Research
         * Certifications
         * Achievements
         *
         * are intentionally NEVER rewritten.
         */


        applyObjectiveSafely(
                resume,
                originalResumeText,
                request,
                baseOptimization
        );


        reorderExistingSkills(
                resume,
                baseOptimization
        );


        optimizeExperienceSelectively(
                resume,
                request
        );


        optimizeProjectsSelectively(
                resume,
                request
        );


        return resume;
    }


    // =========================================================
    // OBJECTIVE
    // =========================================================

    private void applyObjectiveSafely(
            StructuredResume resume,
            String originalResumeText,
            ResumeOptimizationRequest request,
            ResumeOptimizationResult optimization) {

        if (optimization == null) {
            return;
        }


        String candidate =
                safe(
                    optimization
                        .getOptimizedSummary()
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();


        if (candidate.isBlank()) {

            return;
        }


        boolean supported =
                factPreservationValidator
                        .isSupportedSummary(
                                originalResumeText,
                                candidate,
                                request.getRole()
                        );


        /*
         * Unsupported summary?
         *
         * Keep original Career Objective.
         */
        if (!supported) {

            return;
        }


        resume.setCareerObjective(
                candidate
        );
    }


    // =========================================================
    // EXPERIENCE
    // =========================================================

    private void optimizeExperienceSelectively(
            StructuredResume resume,
            ResumeOptimizationRequest request) {

        if (resume.getExperience() == null) {

            return;
        }


        for (ExperienceEntry entry :
                resume.getExperience()) {

            if (entry == null
                    ||
                entry.getBullets() == null
                    ||
                entry.getBullets().isEmpty()) {

                continue;
            }


            /*
             * If every bullet is already strong,
             * don't even call AI.
             */
            if (!containsWeakBullet(
                    entry.getBullets()
            )) {

                continue;
            }


            BulletOptimizationRequest aiRequest =
                    new BulletOptimizationRequest();


            aiRequest.setEntryType(
                    "EXPERIENCE"
            );


            aiRequest.setTitle(
                    entry.getRole()
            );


            aiRequest.setOrganization(
                    entry.getCompany()
            );


            aiRequest.setDuration(
                    entry.getDuration()
            );


            aiRequest.setOriginalBullets(
                    new ArrayList<>(
                            entry.getBullets()
                    )
            );


            aiRequest.setTargetCompany(
                    request.getCompany()
            );


            aiRequest.setTargetRole(
                    request.getRole()
            );


            aiRequest.setJobDescription(
                    request.getJobDescription()
            );


            try {

                BulletOptimizationResult aiResult =
                        structuredAiService
                                .optimizeBullets(
                                        aiRequest
                                );


                entry.setBullets(
                        selectBestBullets(
                                entry.getBullets(),
                                aiResult,
                                ""
                        )
                );


            } catch (Exception ignored) {

                /*
                 * Production fallback:
                 *
                 * Ollama failure should NEVER destroy
                 * resume generation.
                 *
                 * Original bullets stay intact.
                 */
            }
        }
    }


    // =========================================================
    // PROJECTS
    // =========================================================

    private void optimizeProjectsSelectively(
            StructuredResume resume,
            ResumeOptimizationRequest request) {

        if (resume.getProjects() == null) {

            return;
        }


        for (ProjectEntry project :
                resume.getProjects()) {

            if (project == null
                    ||
                project.getBullets() == null
                    ||
                project.getBullets().isEmpty()) {

                continue;
            }


            if (!containsWeakBullet(
                    project.getBullets()
            )) {

                /*
                 * Strong quantified project:
                 * preserve exactly.
                 */
                continue;
            }


            BulletOptimizationRequest aiRequest =
                    new BulletOptimizationRequest();


            aiRequest.setEntryType(
                    "PROJECT"
            );


            aiRequest.setTitle(
                    project.getName()
            );


            aiRequest.setTechnologies(
                    project.getTechnologies()
            );


            aiRequest.setOriginalBullets(
                    new ArrayList<>(
                            project.getBullets()
                    )
            );


            aiRequest.setTargetCompany(
                    request.getCompany()
            );


            aiRequest.setTargetRole(
                    request.getRole()
            );


            aiRequest.setJobDescription(
                    request.getJobDescription()
            );


            try {

                BulletOptimizationResult aiResult =
                        structuredAiService
                                .optimizeBullets(
                                        aiRequest
                                );


                project.setBullets(
                        selectBestBullets(
                                project.getBullets(),
                                aiResult,
                                safe(
                                    project.getTechnologies()
                                )
                        )
                );


            } catch (Exception ignored) {

                /*
                 * Keep original project on AI failure.
                 */
            }
        }
    }


    // =========================================================
    // ORIGINAL VS AI
    // =========================================================

    private List<String> selectBestBullets(
            List<String> originals,
            BulletOptimizationResult aiResult,
            String extraEvidence) {

        List<String> finalBullets =
                new ArrayList<>(
                        originals
                );


        if (aiResult == null
                ||
            aiResult.getRewrites() == null
                ||
            aiResult.getRewrites().isEmpty()) {

            return finalBullets;
        }


        Map<Integer, String> candidates =
                new HashMap<>();


        for (BulletRewrite rewrite :
                aiResult.getRewrites()) {

            if (rewrite == null
                    ||
                rewrite.getSourceIndex() == null
                    ||
                rewrite.getText() == null
                    ||
                rewrite.getText().isBlank()) {

                continue;
            }


            int index =
                    rewrite.getSourceIndex();


            if (index < 0
                    ||
                index >= originals.size()) {

                continue;
            }


            candidates.putIfAbsent(
                    index,
                    cleanBullet(
                            rewrite.getText()
                    )
            );
        }


        for (int index = 0;
             index < originals.size();
             index++) {

            String original =
                    originals.get(
                            index
                    );


            /*
             * Strong original?
             * AI is NOT allowed to touch it.
             */
            if (bulletQualityScorer
                    .isStrong(
                            original
                    )) {

                continue;
            }


            String candidate =
                    candidates.get(
                            index
                    );


            if (candidate == null
                    ||
                candidate.isBlank()) {

                continue;
            }


            boolean factSafe =
                    factPreservationValidator
                            .isSafeRewrite(
                                    original,
                                    candidate,
                                    extraEvidence
                            );


            if (!factSafe) {

                continue;
            }


            boolean better =
                    bulletQualityScorer
                            .isMeaningfulImprovement(
                                    original,
                                    candidate
                            );


            if (!better) {

                continue;
            }


            /*
             * Only here AI wins.
             */
            finalBullets.set(
                    index,
                    candidate
            );
        }


        return finalBullets;
    }


    private boolean containsWeakBullet(
            List<String> bullets) {

        for (String bullet :
                bullets) {

            if (!bulletQualityScorer
                    .isStrong(
                            bullet
                    )) {

                return true;
            }
        }


        return false;
    }


    // =========================================================
    // SKILLS — REORDER ONLY
    // =========================================================

    private void reorderExistingSkills(
            StructuredResume resume,
            ResumeOptimizationResult optimization) {

        if (resume.getSkillCategories() == null
                ||
            resume.getSkillCategories().isEmpty()
                ||
            optimization == null
                ||
            optimization
                .getSkillsToHighlight()
                == null
                ||
            optimization
                .getSkillsToHighlight()
                .isEmpty()) {

            return;
        }


        Set<String> highlighted =
                new LinkedHashSet<>();


        for (String value :
                optimization
                    .getSkillsToHighlight()) {

            if (value == null
                    ||
                value.isBlank()) {

                continue;
            }


            String canonical =
                    SkillCatalog
                            .resolveCanonicalSkill(
                                    value
                            );


            highlighted.add(
                    normalizeSkill(
                            canonical == null
                                    ? value
                                    : canonical
                    )
            );
        }


        /*
         * Reorder skills INSIDE each category.
         *
         * Nothing gets added.
         * Nothing gets deleted.
         */
        for (SkillCategory category :
                resume.getSkillCategories()) {

            if (category == null
                    ||
                category.getSkills() == null
                    ||
                category.getSkills().isEmpty()) {

                continue;
            }


            List<IndexedSkill> indexed =
                    new ArrayList<>();


            for (int i = 0;
                 i < category
                        .getSkills()
                        .size();
                 i++) {

                String skill =
                        category
                            .getSkills()
                            .get(i);


                indexed.add(
                        new IndexedSkill(
                                skill,
                                i,
                                isHighlighted(
                                        skill,
                                        highlighted
                                )
                        )
                );
            }


            indexed.sort(
                    Comparator
                        .comparing(
                            IndexedSkill::highlighted
                        )
                        .reversed()
                        .thenComparingInt(
                            IndexedSkill::originalIndex
                        )
            );


            List<String> reordered =
                    new ArrayList<>();


            for (IndexedSkill value :
                    indexed) {

                reordered.add(
                        value.skill()
                );
            }


            category.setSkills(
                    reordered
            );
        }


        /*
         * Relevant categories first,
         * but original relative order is preserved.
         */
        List<IndexedCategory> categories =
                new ArrayList<>();


        for (int i = 0;
             i < resume
                    .getSkillCategories()
                    .size();
             i++) {

            SkillCategory category =
                    resume
                        .getSkillCategories()
                        .get(i);


            categories.add(
                    new IndexedCategory(
                            category,
                            i,
                            categoryHasHighlight(
                                    category,
                                    highlighted
                            )
                    )
            );
        }


        categories.sort(
                Comparator
                    .comparing(
                        IndexedCategory::highlighted
                    )
                    .reversed()
                    .thenComparingInt(
                        IndexedCategory::originalIndex
                    )
        );


        List<SkillCategory> reorderedCategories =
                new ArrayList<>();


        for (IndexedCategory category :
                categories) {

            reorderedCategories.add(
                    category.category()
            );
        }


        resume.setSkillCategories(
                reorderedCategories
        );
    }


    private boolean categoryHasHighlight(
            SkillCategory category,
            Set<String> highlighted) {

        if (category == null
                ||
            category.getSkills() == null) {

            return false;
        }


        for (String skill :
                category.getSkills()) {

            if (isHighlighted(
                    skill,
                    highlighted
            )) {

                return true;
            }
        }


        return false;
    }


    private boolean isHighlighted(
            String skill,
            Set<String> highlighted) {

        String canonical =
                SkillCatalog
                        .resolveCanonicalSkill(
                                skill
                        );


        String normalized =
                normalizeSkill(
                        canonical == null
                                ? skill
                                : canonical
                );


        return highlighted.contains(
                normalized
        );
    }


    private String normalizeSkill(
            String value) {

        return safe(value)
                .toLowerCase()
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }


    private String cleanBullet(
            String value) {

        return safe(value)
                .trim()
                .replaceFirst(
                        "^[•●▪◦*-]\\s*",
                        ""
                );
    }


    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }


    private record IndexedSkill(
            String skill,
            int originalIndex,
            boolean highlighted) {
    }


    private record IndexedCategory(
            SkillCategory category,
            int originalIndex,
            boolean highlighted) {
    }
}