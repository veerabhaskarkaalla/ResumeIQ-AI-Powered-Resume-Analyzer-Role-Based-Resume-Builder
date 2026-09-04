import {
  useEffect,
  useState,
} from "react";


import type {
  ReactNode,
} from "react";


import {
  deleteResume,
  downloadResumeFile,
  getDashboard,
} from "./api";


import type {
  DashboardResponse,
  ResumeVersionSummary,
} from "./types";


import "./Dashboard.css";


interface DashboardPanelProps {
  refreshKey: number;

  onUseResume:
    (resumeId: number) => void;

  onResumeDeleted:
    (resumeId: number) => void;
}


function DashboardPanel({
  refreshKey,
  onUseResume,
  onResumeDeleted,
}: DashboardPanelProps) {

  const [
    dashboard,
    setDashboard,
  ] =
    useState<DashboardResponse | null>(
      null
    );


  const [loading, setLoading] =
    useState(true);


  const [
    deletingId,
    setDeletingId,
  ] =
    useState<number | null>(
      null
    );


  const [error, setError] =
    useState("");


  const [message, setMessage] =
    useState("");


  async function loadDashboard() {

    try {

      setLoading(true);

      setError("");


      const data =
        await getDashboard();


      setDashboard(
        data
      );


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Dashboard loading failed"
      );

    } finally {

      setLoading(false);
    }
  }


  useEffect(() => {

    loadDashboard();

  }, [refreshKey]);


  async function handleDelete(
    resumeId: number,
    fileName: string
  ) {

    const confirmed =
      window.confirm(
        `Delete "${fileName}"?\n\nThis will permanently delete the resume, ATS history, optimization history and all generated versions.`
      );


    if (!confirmed) {
      return;
    }


    try {

      setError("");

      setMessage("");

      setDeletingId(
        resumeId
      );


      await deleteResume(
        resumeId
      );


      onResumeDeleted(
        resumeId
      );


      setMessage(
        "Resume deleted successfully."
      );


      await loadDashboard();


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Resume deletion failed"
      );

    } finally {

      setDeletingId(null);
    }
  }


  async function download(
    version: ResumeVersionSummary,
    type: "pdf" | "docx"
  ) {

    try {

      setError("");


      await downloadResumeFile(
        version.id,
        type
      );


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Download failed"
      );
    }
  }


  if (loading) {

    return (
      <div className="dashboard-loading">
        Loading dashboard...
      </div>
    );
  }


  if (
    error &&
    !dashboard
  ) {

    return (
      <div className="error-box">
        {error}
      </div>
    );
  }


  if (!dashboard) {
    return null;
  }


  return (
    <div className="dashboard-page">

      <div className="dashboard-header">

        <div>

          <h2>
            My ResumeIQ Dashboard
          </h2>

          <p>
            Resumes, ATS analyses,
            optimized versions and
            score history.
          </p>

        </div>


        <button
          className="dashboard-refresh"
          onClick={
            loadDashboard
          }
        >
          Refresh
        </button>

      </div>


      {error && (

        <div className="error-box">
          {error}
        </div>

      )}


      {message && (

        <div className="dashboard-success">
          {message}
        </div>

      )}


      <div className="dashboard-stats">

        <StatCard
          label="Resumes"
          value={
            dashboard.totalResumes
          }
        />


        <StatCard
          label="ATS Analyses"
          value={
            dashboard.totalAnalyses
          }
        />


        <StatCard
          label="Versions"
          value={
            dashboard.totalVersions
          }
        />


        <StatCard
          label="Optimizations"
          value={
            dashboard.totalOptimizations
          }
        />

      </div>


      {
        dashboard.resumes.length === 0
          ? (

            <div className="dashboard-empty">

              <h3>
                No resumes yet
              </h3>

              <p>
                Upload your first resume
                from ATS Analyzer.
              </p>

            </div>

          )
          : (

            <div className="dashboard-resumes">

              {
                dashboard.resumes.map(
                  (resume) => (

                    <article
                      className="dashboard-resume-card"
                      key={
                        resume.resumeId
                      }
                    >

                      <div className="resume-card-head">

                        <div>

                          <span className="resume-id">
                            RESUME #{resume.resumeId}
                          </span>


                          <h3>
                            {resume.fileName}
                          </h3>


                          <small>
                            Uploaded{" "}
                            {
                              formatDate(
                                resume.uploadedAt
                              )
                            }
                          </small>

                        </div>


                        <div className="resume-card-actions">

                          <div className="latest-score">

                            <span>
                              Latest score
                            </span>

                            <strong>
                              {
                                resume.latestScore
                                ?? "—"
                              }
                            </strong>

                          </div>


                          <div className="resume-action-buttons">

                            <button
                              className="use-resume-button"
                              onClick={() =>
                                onUseResume(
                                  resume.resumeId
                                )
                              }
                            >
                              Use Resume
                            </button>


                            <button
                              className="delete-resume-button"
                              disabled={
                                deletingId ===
                                resume.resumeId
                              }
                              onClick={() =>
                                handleDelete(
                                  resume.resumeId,
                                  resume.fileName
                                )
                              }
                            >

                              {
                                deletingId ===
                                resume.resumeId
                                  ? "Deleting..."
                                  : "Delete"
                              }

                            </button>

                          </div>

                        </div>

                      </div>


                      <DashboardSection
                        title="ATS Analysis History"
                      >

                        {
                          resume.analyses.length === 0
                            ? (

                              <EmptyText
                                text="No ATS analyses yet."
                              />

                            )
                            : (

                              <div className="history-table">

                                {
                                  resume.analyses.map(
                                    (analysis) => (

                                      <div
                                        className="history-row"
                                        key={
                                          analysis.id
                                        }
                                      >

                                        <div>

                                          <strong>
                                            {
                                              analysis.company
                                            }
                                          </strong>

                                          <span>
                                            {
                                              analysis.role
                                            }
                                          </span>

                                        </div>


                                        <ScoreBox
                                          label="ATS"
                                          value={
                                            analysis.atsScore
                                          }
                                        />


                                        <ScoreBox
                                          label="AI"
                                          value={
                                            analysis.semanticScore
                                          }
                                        />


                                        <ScoreBox
                                          label="Final"
                                          value={
                                            analysis.finalScore
                                          }
                                        />


                                        <small>
                                          {
                                            formatDate(
                                              analysis.createdAt
                                            )
                                          }
                                        </small>

                                      </div>

                                    )
                                  )
                                }

                              </div>

                            )
                        }

                      </DashboardSection>


                      <DashboardSection
                        title="Optimization History"
                      >

                        {
                          resume.optimizations.length === 0
                            ? (

                              <EmptyText
                                text="No optimizations yet."
                              />

                            )
                            : (

                              <div className="optimization-list">

                                {
                                  resume.optimizations.map(
                                    (item) => (

                                      <div
                                        className="optimization-history"
                                        key={
                                          item.id
                                        }
                                      >

                                        <div>

                                          <strong>
                                            {
                                              item.company
                                            }
                                          </strong>

                                          <span>
                                            {
                                              item.role
                                            }
                                          </span>

                                        </div>


                                        <div className="history-improvement">

                                          <span>
                                            {
                                              item.beforeScore
                                            }
                                          </span>

                                          <b>
                                            →
                                          </b>

                                          <span>
                                            {
                                              item.afterScore
                                            }
                                          </span>

                                          <strong
                                            className={
                                              item.improvement >= 0
                                                ? "improvement-positive"
                                                : "improvement-negative"
                                            }
                                          >

                                            {
                                              item.improvement >= 0
                                                ? "+"
                                                : ""
                                            }

                                            {
                                              item.improvement
                                            }

                                          </strong>

                                        </div>

                                      </div>

                                    )
                                  )
                                }

                              </div>

                            )
                        }

                      </DashboardSection>


                      <DashboardSection
                        title="Resume Versions"
                      >

                        {
                          resume.versions.length === 0
                            ? (

                              <EmptyText
                                text="No saved versions yet."
                              />

                            )
                            : (

                              <div className="versions-list">

                                {
                                  resume.versions.map(
                                    (version) => (

                                      <div
                                        className="version-row"
                                        key={
                                          version.id
                                        }
                                      >

                                        <div>

                                          <strong>
                                            Version{" "}
                                            {
                                              version.versionNumber
                                            }
                                          </strong>

                                          <span>

                                            {
                                              version.versionType
                                            }

                                            {
                                              version.company
                                                ? ` · ${version.company}`
                                                : ""
                                            }

                                            {
                                              version.role
                                                ? ` · ${version.role}`
                                                : ""
                                            }

                                          </span>

                                        </div>


                                        <div className="version-score">

                                          ATS{" "}
                                          {
                                            version.atsScore
                                            ?? "—"
                                          }

                                        </div>


                                        <div className="version-actions">

                                          <button
                                            onClick={() =>
                                              download(
                                                version,
                                                "pdf"
                                              )
                                            }
                                          >
                                            PDF
                                          </button>


                                          <button
                                            onClick={() =>
                                              download(
                                                version,
                                                "docx"
                                              )
                                            }
                                          >
                                            DOCX
                                          </button>

                                        </div>

                                      </div>

                                    )
                                  )
                                }

                              </div>

                            )
                        }

                      </DashboardSection>

                    </article>

                  )
                )
              }

            </div>

          )
      }

    </div>
  );
}


function StatCard({
  label,
  value,
}: {
  label: string;
  value: number;
}) {

  return (
    <div className="dashboard-stat">

      <span>
        {label}
      </span>

      <strong>
        {value}
      </strong>

    </div>
  );
}


function DashboardSection({
  title,
  children,
}: {
  title: string;
  children: ReactNode;
}) {

  return (
    <section className="dashboard-section">

      <h4>
        {title}
      </h4>

      {children}

    </section>
  );
}


function ScoreBox({
  label,
  value,
}: {
  label: string;
  value: number;
}) {

  return (
    <div className="history-score">

      <small>
        {label}
      </small>

      <strong>
        {value}
      </strong>

    </div>
  );
}


function EmptyText({
  text,
}: {
  text: string;
}) {

  return (
    <p className="history-empty">
      {text}
    </p>
  );
}


function formatDate(
  value: string
): string {

  if (!value) {
    return "";
  }


  return new Date(
    value
  ).toLocaleString();
}


export default DashboardPanel;