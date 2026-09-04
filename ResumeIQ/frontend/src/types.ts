export interface Resume {
  id: number;
  userId?: number;
  fileName: string;
  fileType: string;
  extractedText: string;
  uploadedAt: string;
}


export interface FinalAnalysisResult {
  resumeId: number;

  company: string;
  role: string;

  atsScore: number;
  semanticScore: number;
  finalScore: number;

  keywordMatch: number;
  skillsMatch: number;

  experienceRelevance: number;
  projectsRelevance: number;

  structure: number;
  formatting: number;
  quantification: number;

  matchedSkills: string[];
  missingSkills: string[];

  verifiedStrengths: string[];
  rejectedStrengths: string[];

  gaps: string[];
  suggestions: string[];

  aiSummary: string;
  recommendation: string;
}


export interface OptimizationComparisonResult {
  resumeId: number;

  optimizedVersionId: number;

  versionNumber: number;

  company: string;
  role: string;

  // =========================================================
  // OVERALL
  // =========================================================

  beforeScore: number;
  afterScore: number;
  improvement: number;

  // =========================================================
  // KEYWORDS
  // =========================================================

  beforeKeywordMatch: number;
  afterKeywordMatch: number;

  // =========================================================
  // SKILLS
  // =========================================================

  beforeSkillsMatch: number;
  afterSkillsMatch: number;

  // =========================================================
  // EXPERIENCE
  // =========================================================

  beforeExperienceRelevance: number;
  afterExperienceRelevance: number;

  // =========================================================
  // PROJECTS
  // =========================================================

  beforeProjectsRelevance: number;
  afterProjectsRelevance: number;

  // =========================================================
  // EDUCATION
  // =========================================================

  beforeEducationFit: number;
  afterEducationFit: number;

  // =========================================================
  // STRUCTURE
  // =========================================================

  beforeStructure: number;
  afterStructure: number;

  // =========================================================
  // FORMATTING
  // =========================================================

  beforeFormatting: number;
  afterFormatting: number;

  // =========================================================
  // QUANTIFICATION
  // =========================================================

  beforeQuantification: number;
  afterQuantification: number;

  // =========================================================
  // OUTPUT
  // =========================================================

  optimizedResumeText: string;

  skillsToHighlight: string[];

  missingSkillsNotAdded: string[];

  rejectedExperienceBullets: string[];

  rejectedProjectBullets: string[];

  status: string;
}


export interface ResumeVersionSummary {
  id: number;

  versionNumber: number;

  versionType: string;

  company: string | null;

  role: string | null;

  atsScore: number | null;

  createdAt: string;
}


export interface AtsHistorySummary {
  id: number;

  company: string;

  role: string;

  atsScore: number;

  semanticScore: number;

  finalScore: number;

  keywordMatch: number;

  skillsMatch: number;

  createdAt: string;
}


export interface OptimizationHistorySummary {
  id: number;

  optimizedVersionId: number;

  company: string;

  role: string;

  beforeScore: number;

  afterScore: number;

  improvement: number;

  createdAt: string;
}


export interface DashboardResumeItem {
  resumeId: number;

  fileName: string;

  fileType: string;

  uploadedAt: string;

  latestScore: number | null;

  versions: ResumeVersionSummary[];

  analyses: AtsHistorySummary[];

  optimizations: OptimizationHistorySummary[];
}


export interface DashboardResponse {
  totalResumes: number;

  totalAnalyses: number;

  totalVersions: number;

  totalOptimizations: number;

  resumes: DashboardResumeItem[];
}


export interface UserProfileResponse {
  id: number;

  name: string;

  email: string;

  createdAt: string;
}