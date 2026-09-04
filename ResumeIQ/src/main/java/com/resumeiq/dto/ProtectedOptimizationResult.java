package com.resumeiq.dto;

public class ProtectedOptimizationResult {

    private StructuredResume structuredResume;

    private String resumeText;

    private AtsAnalysisResult atsResult;

    private boolean rollbackApplied;

    private String protectionMessage;


    public ProtectedOptimizationResult() {
    }


    public StructuredResume getStructuredResume() {
        return structuredResume;
    }


    public void setStructuredResume(
            StructuredResume structuredResume) {

        this.structuredResume =
                structuredResume;
    }


    public String getResumeText() {
        return resumeText;
    }


    public void setResumeText(
            String resumeText) {

        this.resumeText =
                resumeText;
    }


    public AtsAnalysisResult getAtsResult() {
        return atsResult;
    }


    public void setAtsResult(
            AtsAnalysisResult atsResult) {

        this.atsResult =
                atsResult;
    }


    public boolean isRollbackApplied() {
        return rollbackApplied;
    }


    public void setRollbackApplied(
            boolean rollbackApplied) {

        this.rollbackApplied =
                rollbackApplied;
    }


    public String getProtectionMessage() {
        return protectionMessage;
    }


    public void setProtectionMessage(
            String protectionMessage) {

        this.protectionMessage =
                protectionMessage;
    }
}