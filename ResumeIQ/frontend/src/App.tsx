import {
  useState,
} from "react";


import {
  analyzeResume,
  downloadResumeFile,
  getResumeById,
  optimizeResume,
  uploadResume,
} from "./api";


import DashboardPanel
  from "./DashboardPanel";


import ProfilePanel
  from "./ProfilePanel";


import type {
  FinalAnalysisResult,
  OptimizationComparisonResult,
  Resume,
} from "./types";


type TabType =
  | "dashboard"
  | "analyzer"
  | "builder"
  | "profile";


type ComponentScore = {
  label: string;
  weight: number;
  before: number;
  after: number;
};


function App() {

  const [
    activeTab,
    setActiveTab,
  ] =
    useState<TabType>(
      "dashboard"
    );


  const [
    file,
    setFile,
  ] =
    useState<File | null>(
      null
    );


  const [
    resume,
    setResume,
  ] =
    useState<Resume | null>(
      null
    );


  const [
    company,
    setCompany,
  ] =
    useState("");


  const [
    role,
    setRole,
  ] =
    useState("");


  const [
    jobDescription,
    setJobDescription,
  ] =
    useState("");


  const [
    analysis,
    setAnalysis,
  ] =
    useState<FinalAnalysisResult | null>(
      null
    );


  const [
    optimization,
    setOptimization,
  ] =
    useState<OptimizationComparisonResult | null>(
      null
    );


  const [
    uploading,
    setUploading,
  ] =
    useState(false);


  const [
    analyzing,
    setAnalyzing,
  ] =
    useState(false);


  const [
    optimizing,
    setOptimizing,
  ] =
    useState(false);


  const [
    loadingResume,
    setLoadingResume,
  ] =
    useState(false);


  const [
    error,
    setError,
  ] =
    useState("");


  const [
    dashboardRefreshKey,
    setDashboardRefreshKey,
  ] =
    useState(0);


  // =========================================================
  // DASHBOARD REFRESH
  // =========================================================

  function refreshDashboard() {

    setDashboardRefreshKey(
      (value) =>
        value + 1
    );
  }


  // =========================================================
  // UPLOAD
  // =========================================================

  async function handleUpload() {

    if (!file) {

      setError(
        "Please select a PDF or DOCX resume."
      );

      return;
    }


    try {

      setError("");

      setUploading(
        true
      );


      const uploaded =
        await uploadResume(
          file
        );


      setResume(
        uploaded
      );


      setAnalysis(
        null
      );


      setOptimization(
        null
      );


      refreshDashboard();


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Resume upload failed."
      );


    } finally {

      setUploading(
        false
      );
    }
  }


  // =========================================================
  // USE EXISTING RESUME
  // =========================================================

  async function handleUseExistingResume(
    resumeId: number
  ) {

    try {

      setError("");

      setLoadingResume(
        true
      );


      const selectedResume =
        await getResumeById(
          resumeId
        );


      setResume(
        selectedResume
      );


      setFile(
        null
      );


      setAnalysis(
        null
      );


      setOptimization(
        null
      );


      setCompany("");

      setRole("");

      setJobDescription("");


      setActiveTab(
        "analyzer"
      );


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Unable to load resume."
      );


    } finally {

      setLoadingResume(
        false
      );
    }
  }


  // =========================================================
  // RESUME DELETED
  // =========================================================

  function handleResumeDeleted(
    resumeId: number
  ) {

    if (
      resume?.id === resumeId
    ) {

      setResume(
        null
      );

      setFile(
        null
      );

      setAnalysis(
        null
      );

      setOptimization(
        null
      );

      setCompany("");

      setRole("");

      setJobDescription("");
    }


    refreshDashboard();
  }


  // =========================================================
  // VALIDATE TARGET
  // =========================================================

  function validateTarget():
    boolean {

    if (!resume) {

      setError(
        "First upload or select a resume."
      );

      return false;
    }


    if (!company.trim()) {

      setError(
        "Enter target company."
      );

      return false;
    }


    if (!role.trim()) {

      setError(
        "Enter target role."
      );

      return false;
    }


    if (!jobDescription.trim()) {

      setError(
        "Paste the job description."
      );

      return false;
    }


    return true;
  }


  // =========================================================
  // ANALYZE
  // =========================================================

  async function handleAnalyze() {

    if (
      !validateTarget() ||
      !resume
    ) {

      return;
    }


    try {

      setError("");

      setAnalyzing(
        true
      );


      const result =
        await analyzeResume(
          resume.id,
          company,
          role,
          jobDescription
        );


      setAnalysis(
        result
      );


      refreshDashboard();


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "ATS analysis failed."
      );


    } finally {

      setAnalyzing(
        false
      );
    }
  }


  // =========================================================
  // OPTIMIZE
  // =========================================================

  async function handleOptimize() {

    if (
      !validateTarget() ||
      !resume
    ) {

      return;
    }


    try {

      setError("");

      setOptimizing(
        true
      );


      const result =
        await optimizeResume(
          resume.id,
          company,
          role,
          jobDescription
        );


      setOptimization(
        result
      );


      refreshDashboard();


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Resume optimization failed."
      );


    } finally {

      setOptimizing(
        false
      );
    }
  }


  // =========================================================
  // UI
  // =========================================================

  return (

    <div className="app-shell">

      <header className="topbar">

        <div>

          <h1>
            Rabbit AI Resume Intelligence
          </h1>

          <p>
            AI-Powered ATS Analysis,
            Job Matching & Resume Optimization
          </p>

        </div>


        <div className="prototype-label">
          RABBIT AI · RESUME INTELLIGENCE ENGINE
        </div>

      </header>


      <div className="tabs">

        <button
          className={
            activeTab === "dashboard"
              ? "tab active"
              : "tab"
          }
          onClick={() => {

            setActiveTab(
              "dashboard"
            );

            refreshDashboard();

          }}
        >
          Dashboard
        </button>


        <button
          className={
            activeTab === "analyzer"
              ? "tab active"
              : "tab"
          }
          onClick={() =>
            setActiveTab(
              "analyzer"
            )
          }
        >
          ATS Analyzer
        </button>


        <button
          className={
            activeTab === "builder"
              ? "tab active"
              : "tab"
          }
          onClick={() =>
            setActiveTab(
              "builder"
            )
          }
        >
          Role-Based Builder
        </button>


        <button
          className={
            activeTab === "profile"
              ? "tab active"
              : "tab"
          }
          onClick={() =>
            setActiveTab(
              "profile"
            )
          }
        >
          Profile
        </button>

      </div>


      {error && (

        <div className="error-box">
          {error}
        </div>

      )}


      {loadingResume && (

        <div className="success-box">
          Loading selected resume...
        </div>

      )}


      {
        activeTab === "dashboard"

          ? (

            <main
              style={{
                paddingTop: "26px",
              }}
            >

              <DashboardPanel
                refreshKey={
                  dashboardRefreshKey
                }
                onUseResume={
                  handleUseExistingResume
                }
                onResumeDeleted={
                  handleResumeDeleted
                }
              />

            </main>

          )

          : activeTab === "profile"

          ? (

            <ProfilePanel />

          )

          : (

            <main className="workspace">

              <section className="left-panel">

                <SectionTitle
                  number="1"
                  title="Resume"
                />


                <div className="upload-card">

                  <input
                    id="resume-file"
                    type="file"
                    accept=".pdf,.docx"
                    onChange={(event) => {

                      const selected =
                        event
                          .target
                          .files?.[0]
                        ?? null;


                      setFile(
                        selected
                      );


                      setError("");

                    }}
                  />


                  <label
                    htmlFor="resume-file"
                    className="upload-drop"
                  >

                    <strong>

                      {
                        file
                          ? file.name

                          : resume
                          ? resume.fileName

                          : "Choose PDF / DOCX"
                      }

                    </strong>


                    <span>

                      {
                        resume
                          ? `Selected Resume ID #${resume.id}`
                          : "Maximum recommended size: 10 MB"
                      }

                    </span>

                  </label>


                  <button
                    className="primary-button"
                    onClick={
                      handleUpload
                    }
                    disabled={
                      uploading ||
                      !file
                    }
                  >

                    {
                      uploading
                        ? "Uploading..."
                        : "Upload New Resume"
                    }

                  </button>


                  {resume && (

                    <div className="success-box">

                      <strong>
                        Active Resume
                      </strong>

                      <span>
                        ID #{resume.id}
                        {" · "}
                        {resume.fileName}
                      </span>

                    </div>

                  )}

                </div>


                <SectionTitle
                  number="2"
                  title="Target Role"
                />


                <div className="target-grid">

                  <input
                    value={
                      company
                    }
                    onChange={(event) =>
                      setCompany(
                        event.target.value
                      )
                    }
                    placeholder="Company e.g. Amazon"
                  />


                  <input
                    value={
                      role
                    }
                    onChange={(event) =>
                      setRole(
                        event.target.value
                      )
                    }
                    placeholder="Role e.g. SDE"
                  />

                </div>


                <textarea
                  className="jd-area"
                  value={
                    jobDescription
                  }
                  onChange={(event) =>
                    setJobDescription(
                      event.target.value
                    )
                  }
                  placeholder="Paste the job description here..."
                />


                {
                  activeTab === "analyzer"

                    ? (

                      <button
                        className="primary-button action-button"
                        onClick={
                          handleAnalyze
                        }
                        disabled={
                          analyzing
                        }
                      >

                        {
                          analyzing
                            ? "Analyzing Resume..."
                            : "Run ATS Analysis"
                        }

                      </button>

                    )

                    : (

                      <button
                        className="primary-button action-button"
                        onClick={
                          handleOptimize
                        }
                        disabled={
                          optimizing
                        }
                      >

                        {
                          optimizing
                            ? "Optimizing with Rabbit AI..."
                            : "Generate Optimized Resume"
                        }

                      </button>

                    )
                }


                {resume && (

                  <>

                    <SectionTitle
                      number="3"
                      title="Resume Text"
                    />


                    <div className="resume-preview">

                      {
                        resume.extractedText
                      }

                    </div>

                  </>

                )}

              </section>


              <section className="right-panel">

                {
                  activeTab === "analyzer"

                    ? (

                      analysis

                        ? (

                          <AnalysisReport
                            analysis={
                              analysis
                            }
                          />

                        )

                        : (

                          <EmptyReport
                            text="Select a resume, add target company, role and job description, then run ATS analysis."
                          />

                        )

                    )

                    : (

                      optimization

                        ? (

                          <OptimizationReport
                            result={
                              optimization
                            }
                          />

                        )

                        : (

                          <EmptyReport
                            text="Generate a role-specific optimized resume to compare ATS performance before and after optimization."
                          />

                        )

                    )
                }

              </section>

            </main>

          )
      }

    </div>

  );
}


// =========================================================
// SECTION TITLE
// =========================================================

function SectionTitle({

  number,
  title,

}: {

  number: string;
  title: string;

}) {

  return (

    <div className="section-title">

      <span>
        {number}
      </span>

      <h2>
        {title}
      </h2>

    </div>

  );
}


// =========================================================
// SCORE RING
// =========================================================

function ScoreRing({

  score,
  label,

}: {

  score: number;
  label: string;

}) {

  const safeScore =
    Math.max(
      0,
      Math.min(
        100,
        Number.isFinite(score)
          ? score
          : 0
      )
    );


  return (

    <div className="score-block">

      <div
        className="score-ring"
        style={{
          background:
            `conic-gradient(
              #bb7a05 ${safeScore * 3.6}deg,
              #ded8cb 0deg
            )`,
        }}
      >

        <div className="score-inner">
          {safeScore}
        </div>

      </div>


      <span>
        {label}
      </span>

    </div>

  );
}


// =========================================================
// METRIC BAR
// =========================================================

function MetricBar({

  label,
  value,

}: {

  label: string;
  value: number;

}) {

  const safeValue =
    Math.max(
      0,
      Math.min(
        100,
        Number.isFinite(value)
          ? value
          : 0
      )
    );


  return (

    <div className="metric">

      <div className="metric-title">

        <span>
          {label}
        </span>

        <strong>
          {safeValue}
        </strong>

      </div>


      <div className="metric-track">

        <div
          className="metric-fill"
          style={{
            width:
              `${safeValue}%`,
          }}
        />

      </div>

    </div>

  );
}


// =========================================================
// ANALYSIS REPORT
// =========================================================

function AnalysisReport({

  analysis,

}: {

  analysis:
    FinalAnalysisResult;

}) {

  return (

    <div>

      <SectionTitle
        number="3"
        title="ATS Report"
      />


      <div className="score-row">

        <ScoreRing
          score={
            analysis.finalScore
          }
          label="FINAL"
        />


        <ScoreRing
          score={
            analysis.atsScore
          }
          label="ATS"
        />


        <ScoreRing
          score={
            analysis.semanticScore
          }
          label="SEMANTIC"
        />

      </div>


      <p className="summary-text">
        {analysis.aiSummary}
      </p>


      <div className="metrics">

        <MetricBar
          label="Keyword Match"
          value={
            analysis.keywordMatch
          }
        />


        <MetricBar
          label="Skills Match"
          value={
            analysis.skillsMatch
          }
        />


        <MetricBar
          label="Experience Relevance"
          value={
            analysis.experienceRelevance
          }
        />


        <MetricBar
          label="Projects Relevance"
          value={
            analysis.projectsRelevance
          }
        />


        <MetricBar
          label="Structure"
          value={
            analysis.structure
          }
        />


        <MetricBar
          label="Formatting"
          value={
            analysis.formatting
          }
        />


        <MetricBar
          label="Quantification"
          value={
            analysis.quantification
          }
        />

      </div>


      <TagSection
        title="Matched Skills"
        items={
          analysis.matchedSkills
        }
        type="good"
      />


      <TagSection
        title="Missing Skills"
        items={
          analysis.missingSkills
        }
        type="warning"
      />


      <TagSection
        title="Verified Strengths"
        items={
          analysis.verifiedStrengths
        }
        type="good"
      />


      <TagSection
        title="Gaps"
        items={
          analysis.gaps
        }
        type="danger"
      />


      <TextList
        title="Rabbit AI Suggestions"
        items={
          analysis.suggestions
        }
      />


      <div className="recommendation-box">

        <strong>
          Recommendation
        </strong>

        <p>
          {
            analysis.recommendation
          }
        </p>

      </div>

    </div>

  );
}


// =========================================================
// OPTIMIZATION REPORT
// =========================================================

function OptimizationReport({

  result,

}: {

  result:
    OptimizationComparisonResult;

}) {

  const [
    downloading,
    setDownloading,
  ] =
    useState<
      "pdf" |
      "docx" |
      null
    >(null);


  const [
    downloadError,
    setDownloadError,
  ] =
    useState("");


  // =======================================================
  // ATS COMPONENTS
  // =======================================================

  const componentScores:
    ComponentScore[] = [

      {
        label:
          "Skills Match",

        weight:
          25,

        before:
          metric(
            result.beforeSkillsMatch
          ),

        after:
          metric(
            result.afterSkillsMatch
          ),
      },


      {
        label:
          "Keyword Match",

        weight:
          20,

        before:
          metric(
            result.beforeKeywordMatch
          ),

        after:
          metric(
            result.afterKeywordMatch
          ),
      },


      {
        label:
          "Experience Relevance",

        weight:
          15,

        before:
          metric(
            result.beforeExperienceRelevance
          ),

        after:
          metric(
            result.afterExperienceRelevance
          ),
      },


      {
        label:
          "Projects Relevance",

        weight:
          10,

        before:
          metric(
            result.beforeProjectsRelevance
          ),

        after:
          metric(
            result.afterProjectsRelevance
          ),
      },


      {
        label:
          "Education Fit",

        weight:
          5,

        before:
          metric(
            result.beforeEducationFit
          ),

        after:
          metric(
            result.afterEducationFit
          ),
      },


      {
        label:
          "Structure",

        weight:
          10,

        before:
          metric(
            result.beforeStructure
          ),

        after:
          metric(
            result.afterStructure
          ),
      },


      {
        label:
          "Formatting",

        weight:
          5,

        before:
          metric(
            result.beforeFormatting
          ),

        after:
          metric(
            result.afterFormatting
          ),
      },


      {
        label:
          "Quantification",

        weight:
          10,

        before:
          metric(
            result.beforeQuantification
          ),

        after:
          metric(
            result.afterQuantification
          ),
      },

    ];


  // =======================================================
  // WEAKEST COMPONENT
  // =======================================================

  const weakestBefore =
    componentScores.reduce(
      (lowest, current) =>

        current.before <
        lowest.before

          ? current

          : lowest
    );


  // =======================================================
  // BIGGEST IMPROVEMENT
  // =======================================================

  const biggestGain =
    componentScores.reduce(
      (best, current) => {

        const bestGain =
          best.after -
          best.before;


        const currentGain =
          current.after -
          current.before;


        return currentGain >
          bestGain

          ? current

          : best;

      }
    );


  const biggestGainValue =
    biggestGain.after -
    biggestGain.before;


  // =======================================================
  // WEIGHTED SCORE CHECK
  // =======================================================

  const calculatedBefore =
    calculateWeightedScore(
      componentScores,
      "before"
    );


  const calculatedAfter =
    calculateWeightedScore(
      componentScores,
      "after"
    );


  // =======================================================
  // SCORE MESSAGE
  // =======================================================

  const scoreMessage =
    buildScoreMessage(
      result.beforeScore,
      result.afterScore,
      biggestGainValue,
      weakestBefore
    );


  // =======================================================
  // DOWNLOAD
  // =======================================================

  async function download(

    type:
      | "pdf"
      | "docx"

  ) {

    try {

      setDownloadError("");

      setDownloading(
        type
      );


      await downloadResumeFile(
        result.optimizedVersionId,
        type
      );


    } catch (error) {

      setDownloadError(
        error instanceof Error
          ? error.message
          : "Download failed"
      );


    } finally {

      setDownloading(
        null
      );
    }
  }


  return (

    <div>

      <SectionTitle
        number="3"
        title="Match Report"
      />


      {/* ===================================================
          BEFORE / AFTER SCORE
      =================================================== */}

      <div className="comparison-score">

        <ScoreRing
          score={
            result.beforeScore
          }
          label="BEFORE"
        />


        <div className="arrow">
          →
        </div>


        <ScoreRing
          score={
            result.afterScore
          }
          label="AFTER"
        />


        <div
          className={
            result.improvement > 0
              ? "gain positive"

              : result.improvement < 0
              ? "gain negative"

              : "gain"
          }
        >

          {
            result.improvement > 0
              ? "+"
              : ""
          }

          {result.improvement}

          {" pts"}

        </div>

      </div>


      {/* ===================================================
          ATS COMPONENT BREAKDOWN
      =================================================== */}

      <div className="report-section">

        <h3>
          ATS Component Breakdown
        </h3>


        <div className="comparison-table">

          {componentScores.map(
            (component) => (

              <ComparisonRow
                key={
                  component.label
                }
                label={
                  `${component.label} (${component.weight}%)`
                }
                before={
                  component.before
                }
                after={
                  component.after
                }
              />

            )
          )}

        </div>

      </div>


      {/* ===================================================
          SCORE EXPLANATION
      =================================================== */}

      <div className="recommendation-box">

        <strong>
          Rabbit AI Score Explanation
        </strong>


        <p>
          {scoreMessage}
        </p>


        <p>

          <strong>
            Weakest ATS area:
          </strong>

          {" "}

          {weakestBefore.label}

          {" — "}

          {weakestBefore.before}/100.

        </p>


        <p>

          <strong>
            Biggest component change:
          </strong>

          {" "}

          {biggestGain.label}

          {" "}

          {
            formatDelta(
              biggestGainValue
            )
          }.

        </p>


        <p>

          <strong>
            Weighted ATS calculation:
          </strong>

          {" "}

          {calculatedBefore}

          {" → "}

          {calculatedAfter}

        </p>

      </div>


      {/* ===================================================
          ATS WEIGHT INFORMATION
      =================================================== */}

      <div className="report-section">

        <h3>
          ATS Score Weights
        </h3>


        <div
          className="text-list"
        >

          <p>
            Skills Match — 25%
          </p>

          <p>
            Keyword Match — 20%
          </p>

          <p>
            Experience Relevance — 15%
          </p>

          <p>
            Projects Relevance — 10%
          </p>

          <p>
            Education Fit — 5%
          </p>

          <p>
            Structure — 10%
          </p>

          <p>
            Formatting — 5%
          </p>

          <p>
            Quantification — 10%
          </p>

        </div>

      </div>


      {/* ===================================================
          SKILLS TO HIGHLIGHT
      =================================================== */}

      <TagSection
        title="Skills To Highlight"
        items={
          result.skillsToHighlight
        }
        type="good"
      />


      {/* ===================================================
          MISSING SKILLS
      =================================================== */}

      <TagSection
        title="Missing / Not Added"
        items={
          result.missingSkillsNotAdded
        }
        type="warning"
      />


      {/* ===================================================
          REJECTED EXPERIENCE REWRITES
      =================================================== */}

      <TagSection
        title="Rejected Experience Rewrites"
        items={
          result.rejectedExperienceBullets
        }
        type="warning"
      />


      {/* ===================================================
          REJECTED PROJECT REWRITES
      =================================================== */}

      <TagSection
        title="Rejected Project Rewrites"
        items={
          result.rejectedProjectBullets
        }
        type="warning"
      />


      {/* ===================================================
          OPTIMIZED RESUME
      =================================================== */}

      <div className="optimized-content">

        <h3>
          Optimized Resume
        </h3>


        <pre>
          {
            result.optimizedResumeText
          }
        </pre>

      </div>


      {/* ===================================================
          DOWNLOAD ERROR
      =================================================== */}

      {downloadError && (

        <div className="error-box">
          {downloadError}
        </div>

      )}


      {/* ===================================================
          DOWNLOAD BUTTONS
      =================================================== */}

      <div className="download-row">

        <button
          className="download-button"
          style={{
            border: "none",
          }}
          disabled={
            downloading !== null
          }
          onClick={() =>
            download(
              "pdf"
            )
          }
        >

          {
            downloading === "pdf"
              ? "Downloading PDF..."
              : "Download PDF"
          }

        </button>


        <button
          className="download-button secondary"
          style={{
            border: "none",
          }}
          disabled={
            downloading !== null
          }
          onClick={() =>
            download(
              "docx"
            )
          }
        >

          {
            downloading === "docx"
              ? "Downloading DOCX..."
              : "Download DOCX"
          }

        </button>

      </div>


      {/* ===================================================
          OPTIMIZATION STATUS
      =================================================== */}

      {
        result.status && (

          <div className="recommendation-box">

            <strong>
              Optimization Status
            </strong>

            <p>
              {result.status}
            </p>

          </div>

        )
      }

    </div>

  );
}


// =========================================================
// COMPARISON ROW
// =========================================================

function ComparisonRow({

  label,
  before,
  after,

}: {

  label: string;
  before: number;
  after: number;

}) {

  const difference =
    after - before;


  return (

    <div className="comparison-row">

      <span>
        {label}
      </span>


      <span>
        {before}
      </span>


      <span className="comparison-arrow">
        →
      </span>


      <strong>

        {after}


        <small
          style={{
            marginLeft: "7px",

            opacity:
              0.72,

            fontSize:
              "0.72em",
          }}
        >

          {
            formatDelta(
              difference
            )
          }

        </small>

      </strong>

    </div>

  );
}


// =========================================================
// TAG SECTION
// =========================================================

function TagSection({

  title,
  items,
  type,

}: {

  title: string;

  items?: string[];

  type:
    | "good"
    | "warning"
    | "danger";

}) {

  if (
    !items ||
    items.length === 0
  ) {

    return null;
  }


  return (

    <div className="report-section">

      <h3>
        {title}
      </h3>


      <div className="tag-container">

        {
          items.map(
            (item, index) => (

              <span
                className={
                  `tag ${type}`
                }
                key={
                  `${item}-${index}`
                }
              >

                {item}

              </span>

            )
          )
        }

      </div>

    </div>

  );
}


// =========================================================
// TEXT LIST
// =========================================================

function TextList({

  title,
  items,

}: {

  title: string;

  items?: string[];

}) {

  if (
    !items ||
    items.length === 0
  ) {

    return null;
  }


  return (

    <div className="report-section">

      <h3>
        {title}
      </h3>


      <div className="text-list">

        {
          items.map(
            (item, index) => (

              <p key={index}>

                {index + 1}.

                {" "}

                {item}

              </p>

            )
          )
        }

      </div>

    </div>

  );
}


// =========================================================
// EMPTY REPORT
// =========================================================

function EmptyReport({

  text,

}: {

  text: string;

}) {

  return (

    <div className="empty-report">

      <div className="empty-icon">
        AI
      </div>


      <h2>
        Rabbit AI
      </h2>


      <p>
        {text}
      </p>

    </div>

  );
}


// =========================================================
// METRIC SAFE VALUE
// =========================================================

function metric(
  value:
    | number
    | undefined
    | null
) {

  if (
    value === undefined ||
    value === null ||
    !Number.isFinite(value)
  ) {

    return 0;
  }


  return Math.max(
    0,
    Math.min(
      100,
      value
    )
  );
}


// =========================================================
// FORMAT SCORE CHANGE
// =========================================================

function formatDelta(
  difference: number
) {

  if (
    difference > 0
  ) {

    return `+${difference}`;
  }


  if (
    difference < 0
  ) {

    return `${difference}`;
  }


  return "±0";
}


// =========================================================
// CALCULATE WEIGHTED ATS SCORE
// =========================================================

function calculateWeightedScore(

  components:
    ComponentScore[],

  side:
    | "before"
    | "after"

) {

  const value =
    components.reduce(
      (
        total,
        component
      ) => {

        const score =
          side === "before"
            ? component.before
            : component.after;


        return total +
          score *
          (
            component.weight /
            100
          );

      },
      0
    );


  return Math.round(
    value
  );
}


// =========================================================
// BUILD SCORE EXPLANATION
// =========================================================

function buildScoreMessage(

  beforeScore: number,

  afterScore: number,

  biggestGain: number,

  weakest:
    ComponentScore

) {

  if (
    afterScore >
    beforeScore
  ) {

    return (
      `Rabbit AI improved the ATS score from ` +
      `${beforeScore} to ${afterScore}. ` +
      `The optimization strengthened one or more ` +
      `job-relevant ATS components while preserving ` +
      `the factual evidence from the original resume.`
    );
  }


  if (
    afterScore ===
    beforeScore
  ) {

    if (
      biggestGain > 0
    ) {

      return (
        `The resume improved in some ATS components, ` +
        `but the weighted changes were not large enough ` +
        `to move the final score beyond ${afterScore}. ` +
        `${weakest.label} remains the main improvement opportunity.`
      );
    }


    return (
      `The ATS score remained protected at ${afterScore}. ` +
      `Rabbit AI did not add unsupported skills, experience, ` +
      `projects or achievements simply to increase the score. ` +
      `${weakest.label} is currently the weakest ATS component ` +
      `and should be improved only when the resume contains ` +
      `real supporting evidence.`
    );
  }


  return (
    `The optimized candidate produced a lower ATS score. ` +
    `Score protection should prevent this version from ` +
    `replacing the stronger original resume.`
  );
}


export default App;