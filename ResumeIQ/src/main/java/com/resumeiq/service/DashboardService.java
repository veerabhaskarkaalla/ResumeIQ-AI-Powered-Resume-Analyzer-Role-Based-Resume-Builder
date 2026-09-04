package com.resumeiq.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.resumeiq.dto.AtsHistorySummary;
import com.resumeiq.dto.DashboardResponse;
import com.resumeiq.dto.DashboardResumeItem;
import com.resumeiq.dto.OptimizationHistorySummary;
import com.resumeiq.dto.ResumeVersionSummary;

import com.resumeiq.entity.AtsAnalysisHistory;
import com.resumeiq.entity.OptimizationHistory;
import com.resumeiq.entity.Resume;
import com.resumeiq.entity.ResumeVersion;

import com.resumeiq.repository.AtsAnalysisHistoryRepository;
import com.resumeiq.repository.OptimizationHistoryRepository;
import com.resumeiq.repository.ResumeVersionRepository;

@Service
public class DashboardService {

    private final ResumeService resumeService;

    private final ResumeVersionRepository
            versionRepository;

    private final OptimizationHistoryRepository
            optimizationRepository;

    private final AtsAnalysisHistoryRepository
            analysisRepository;


    public DashboardService(
            ResumeService resumeService,
            ResumeVersionRepository versionRepository,
            OptimizationHistoryRepository optimizationRepository,
            AtsAnalysisHistoryRepository analysisRepository) {

        this.resumeService =
                resumeService;

        this.versionRepository =
                versionRepository;

        this.optimizationRepository =
                optimizationRepository;

        this.analysisRepository =
                analysisRepository;
    }


    public DashboardResponse getDashboard() {

        List<Resume> resumes =
                resumeService.getAllResumes();


        List<DashboardResumeItem> items =
                new ArrayList<>();


        int totalVersions = 0;

        int totalAnalyses = 0;

        int totalOptimizations = 0;


        for (Resume resume : resumes) {

            List<ResumeVersion> versions =
                    versionRepository
                            .findByResumeIdOrderByVersionNumberAsc(
                                    resume.getId()
                            );


            List<AtsAnalysisHistory> analyses =
                    analysisRepository
                            .findByResumeIdOrderByCreatedAtDesc(
                                    resume.getId()
                            );


            List<OptimizationHistory> optimizations =
                    optimizationRepository
                            .findByResumeIdOrderByCreatedAtDesc(
                                    resume.getId()
                            );


            totalVersions +=
                    versions.size();


            totalAnalyses +=
                    analyses.size();


            totalOptimizations +=
                    optimizations.size();


            DashboardResumeItem item =
                    new DashboardResumeItem();


            item.setResumeId(
                    resume.getId()
            );


            item.setFileName(
                    resume.getFileName()
            );


            item.setFileType(
                    resume.getFileType()
            );


            item.setUploadedAt(
                    resume.getUploadedAt()
            );


            if (!analyses.isEmpty()) {

                item.setLatestScore(
                        analyses.get(0)
                                .getFinalScore()
                );

            } else if (!versions.isEmpty()) {

                ResumeVersion latest =
                        versions.get(
                                versions.size() - 1
                        );


                item.setLatestScore(
                        latest.getAtsScore()
                );

            } else {

                item.setLatestScore(null);
            }


            item.setVersions(
                    mapVersions(
                            versions
                    )
            );


            item.setAnalyses(
                    mapAnalyses(
                            analyses
                    )
            );


            item.setOptimizations(
                    mapOptimizations(
                            optimizations
                    )
            );


            items.add(item);
        }


        DashboardResponse response =
                new DashboardResponse();


        response.setTotalResumes(
                resumes.size()
        );


        response.setTotalVersions(
                totalVersions
        );


        response.setTotalAnalyses(
                totalAnalyses
        );


        response.setTotalOptimizations(
                totalOptimizations
        );


        response.setResumes(
                items
        );


        return response;
    }


    private List<ResumeVersionSummary>
            mapVersions(
                    List<ResumeVersion> versions) {

        List<ResumeVersionSummary> result =
                new ArrayList<>();


        for (ResumeVersion version : versions) {

            ResumeVersionSummary item =
                    new ResumeVersionSummary();


            item.setId(
                    version.getId()
            );


            item.setVersionNumber(
                    version.getVersionNumber()
            );


            item.setVersionType(
                    version.getVersionType()
            );


            item.setCompany(
                    version.getCompany()
            );


            item.setRole(
                    version.getRole()
            );


            item.setAtsScore(
                    version.getAtsScore()
            );


            item.setCreatedAt(
                    version.getCreatedAt()
            );


            result.add(item);
        }


        return result;
    }


    private List<AtsHistorySummary>
            mapAnalyses(
                    List<AtsAnalysisHistory> analyses) {

        List<AtsHistorySummary> result =
                new ArrayList<>();


        for (AtsAnalysisHistory analysis :
                analyses) {

            AtsHistorySummary item =
                    new AtsHistorySummary();


            item.setId(
                    analysis.getId()
            );


            item.setCompany(
                    analysis.getCompany()
            );


            item.setRole(
                    analysis.getRole()
            );


            item.setAtsScore(
                    analysis.getAtsScore()
            );


            item.setSemanticScore(
                    analysis.getSemanticScore()
            );


            item.setFinalScore(
                    analysis.getFinalScore()
            );


            item.setKeywordMatch(
                    analysis.getKeywordMatch()
            );


            item.setSkillsMatch(
                    analysis.getSkillsMatch()
            );


            item.setCreatedAt(
                    analysis.getCreatedAt()
            );


            result.add(item);
        }


        return result;
    }


    private List<OptimizationHistorySummary>
            mapOptimizations(
                    List<OptimizationHistory> optimizations) {

        List<OptimizationHistorySummary> result =
                new ArrayList<>();


        for (OptimizationHistory optimization :
                optimizations) {

            OptimizationHistorySummary item =
                    new OptimizationHistorySummary();


            item.setId(
                    optimization.getId()
            );


            item.setOptimizedVersionId(
                    optimization.getOptimizedVersionId()
            );


            item.setCompany(
                    optimization.getCompany()
            );


            item.setRole(
                    optimization.getRole()
            );


            item.setBeforeScore(
                    optimization.getBeforeScore()
            );


            item.setAfterScore(
                    optimization.getAfterScore()
            );


            item.setImprovement(
                    optimization.getImprovement()
            );


            item.setCreatedAt(
                    optimization.getCreatedAt()
            );


            result.add(item);
        }


        return result;
    }
}