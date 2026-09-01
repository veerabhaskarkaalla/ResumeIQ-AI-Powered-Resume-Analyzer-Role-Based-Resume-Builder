# 🐇 Rabbit AI Resume Intelligence

### AI-Powered ATS Analysis, Job Matching & Evidence-Safe Resume Optimization

Rabbit AI Resume Intelligence is a full-stack AI-powered resume intelligence platform designed to analyze resumes against real job descriptions, calculate explainable ATS compatibility scores, identify skill and keyword gaps, perform semantic job matching, and generate optimized resumes without inventing unsupported experience or skills.

The system combines a **deterministic ATS engine**, **AI-based semantic reasoning**, **fact verification**, **resume versioning**, and **professional PDF/DOCX generation** into one end-to-end platform.

---

# 📌 Table of Contents

- [Project Overview](#-project-overview)
- [Problem Statement](#-problem-statement)
- [Project Objectives](#-project-objectives)
- [Core Features](#-core-features)
- [Application Workflow](#-application-workflow)
- [System Architecture](#️-system-architecture)
- [Module Architecture](#-module-architecture)
- [ATS Scoring Engine](#-ats-scoring-engine)
- [Hybrid AI Architecture](#-hybrid-ai-architecture)
- [Job Description Intelligence](#-job-description-intelligence)
- [Resume Optimization Engine](#-resume-optimization-engine)
- [Hallucination Guard](#️-hallucination-guard)
- [ATS Score Protection](#-ats-score-protection)
- [Structured Resume Model](#-structured-resume-model)
- [Resume Parsing](#-resume-parsing)
- [Resume Versioning](#-resume-versioning)
- [PDF and DOCX Export](#-pdf-and-docx-export)
- [Adaptive Resume Layout](#-adaptive-resume-layout)
- [Authentication and Security](#-authentication-and-security)
- [Dashboard](#-dashboard)
- [Technology Stack](#️-technology-stack)
- [Project Structure](#-project-structure)
- [API Workflow](#-api-workflow)
- [Database Design](#️-database-design)
- [Local Setup](#-local-setup)
- [Ollama Setup](#-ollama-setup)
- [AWS Deployment Architecture](#️-aws-deployment-architecture)
- [Production Security Design](#-production-security-design)
- [Example Use Case](#-example-use-case)
- [Testing Strategy](#-testing-strategy)
- [Design Decisions](#-important-design-decisions)
- [Current Limitations](#️-current-limitations)
- [Future Enhancements](#-future-enhancements)
- [Security Guidelines](#-security-guidelines)
- [Project Goals](#-project-goals)

---

# 🚀 Project Overview

Most resume tools perform one of two tasks:

1. Provide a generic ATS score.
2. Send the complete resume to an LLM and ask it to rewrite the content.

Both approaches have limitations.

A generic ATS score does not explain whether a resume is suitable for a **specific company, role, and job description**.

Pure LLM-based resume rewriting can also create another serious problem:

> AI may generate skills, technologies, achievements, experience, or metrics that the candidate never actually had.

Rabbit AI Resume Intelligence solves these problems using a hybrid architecture.

```text
Candidate Resume
       +
Target Company
       +
Target Role
       +
Job Description
       ↓
Rabbit AI Resume Intelligence
       ↓
Resume Parsing
       ↓
Deterministic ATS Analysis
       ↓
Semantic AI Analysis
       ↓
Skill Gap Detection
       ↓
Evidence Verification
       ↓
Safe Resume Optimization
       ↓
ATS Re-Scoring
       ↓
Score Protection
       ↓
Optimized Resume
       ↓
PDF / DOCX Export
```

---

# ❗ Problem Statement

Job seekers frequently apply to multiple jobs using the same resume.

However, every job description contains different:

- Required skills
- Preferred technologies
- Responsibilities
- Keywords
- Experience expectations
- Domain requirements
- Cloud/platform requirements
- Tooling expectations

Because of this, a resume that performs well for one role may perform poorly for another.

Existing resume optimization systems may also blindly insert job-description keywords into a resume, which can result in:

- Fake skills
- Unsupported technologies
- Fabricated achievements
- False experience
- Misleading metrics

Rabbit AI aims to provide a safer alternative.

---

# 🎯 Project Objectives

The main goals of Rabbit AI are:

- Analyze a resume against a specific job description
- Calculate an explainable ATS compatibility score
- Identify matched and missing skills
- Measure keyword alignment
- Evaluate experience relevance
- Evaluate project relevance
- Check resume structure
- Detect measurable achievements
- Perform semantic AI job matching
- Generate job-targeted resume improvements
- Prevent unsupported AI-generated claims
- Recalculate ATS score after optimization
- Protect the original resume if optimization reduces its score
- Maintain resume version history
- Export professional ATS-friendly PDF and DOCX files

---

# ✨ Core Features

## 📄 Resume Upload

Rabbit AI supports:

- PDF resumes
- DOCX resumes

Uploaded resumes are automatically processed and converted into structured resume information.

---

## 🔍 Automatic Resume Parsing

The system extracts:

- Candidate name
- Email
- Phone number
- Location
- Profile links
- Career objective
- Education
- Work experience
- Technical skills
- Projects
- Research publications
- Certifications
- Achievements

---

## 🎯 Role-Based Resume Analysis

Users provide:

```text
Target Company
Target Role
Job Description
```

Example:

```text
Company: Amazon
Role: AWS Cloud Engineer
```

Rabbit AI evaluates the resume specifically for that target role.

---

## 📊 Explainable ATS Scoring

Instead of returning only one unexplained score, Rabbit AI breaks ATS compatibility into several components.

```text
Skills Match
Keyword Match
Experience Relevance
Projects Relevance
Education Fit
Structure
Formatting
Quantification
```

This makes the ATS score easier to understand and debug.

---

## 🧠 Semantic AI Matching

Keyword matching alone is not enough.

For example:

```text
Job Description:
Build scalable backend services.

Resume:
Developed REST APIs using Spring Boot.
```

These sentences may describe related experience even when they do not contain exactly the same words.

Rabbit AI uses AI-based semantic analysis to detect such relationships.

---

## 🛡️ Evidence-Safe Optimization

The system tries to improve:

- Existing experience bullets
- Existing project descriptions
- Existing skill presentation
- Career objective
- Job-specific emphasis

But it does not intentionally fabricate unsupported information.

---

## 📈 Before vs After Comparison

After optimization:

```text
Original Resume
      ↓
ATS Score
      ↓
Optimization
      ↓
Optimized Resume
      ↓
ATS Score
```

The user receives a direct comparison.

---

## 🔒 Score Protection

If the AI-generated resume becomes worse according to the ATS engine, Rabbit AI can reject or rollback the weaker candidate.

---

## 📑 Resume Versioning

Each optimized resume can be maintained as another version.

This allows one user to create resumes for different companies and roles.

---

## 📥 Professional Export

Users can download optimized resumes as:

- PDF
- DOCX

---

# 🔄 Application Workflow

```text
1. User Registration / Login
            ↓
2. Upload PDF or DOCX Resume
            ↓
3. Extract Resume Text
            ↓
4. Parse Resume Sections
            ↓
5. Build Structured Resume
            ↓
6. Enter Target Company
            ↓
7. Enter Target Role
            ↓
8. Paste Job Description
            ↓
9. Analyze Job Description
            ↓
10. Run Deterministic ATS Engine
            ↓
11. Run Semantic AI Analysis
            ↓
12. Show ATS Match Report
            ↓
13. Identify Missing Skills
            ↓
14. Generate Resume Optimization Candidate
            ↓
15. Verify Candidate Against Original Resume
            ↓
16. Reject Unsupported Rewrites
            ↓
17. Recalculate ATS Score
            ↓
18. Apply Score Protection
            ↓
19. Save Resume Version
            ↓
20. Generate PDF / DOCX
            ↓
21. Download Optimized Resume
```

---

# 🏗️ System Architecture

```text
                              USER
                               │
                               ▼
                     React + TypeScript
                          Frontend
                               │
                               │ REST API
                               ▼
                       Spring Boot Backend
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
  Resume Processing       ATS Intelligence        AI Engine
        │                      │                      │
        ├── PDF Parser         ├── Skills             ├── Ollama
        ├── DOCX Parser        ├── Keywords           ├── Qwen3:8b
        ├── Structured         ├── Experience         └── Semantic Analysis
        │   Resume             ├── Projects
        │                      ├── Education
        │                      ├── Structure
        │                      ├── Formatting
        │                      └── Quantification
        │
        ├──────────────────────┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
 Optimization Engine    Hallucination Guard    Version Manager
        │
        ▼
  Score Protection
        │
        ▼
 Resume Export Engine
    │           │
    ▼           ▼
  LaTeX       Apache POI
    │           │
    ▼           ▼
   PDF         DOCX
        │
        ▼
      MySQL
```

---

# 🧩 Module Architecture

Rabbit AI is divided into independent logical modules.

## Resume Management Module

Responsible for:

- Resume upload
- File validation
- Resume persistence
- Resume retrieval
- Resume deletion
- Resume ownership

---

## Resume Parser Module

Responsible for:

- PDF text extraction
- DOCX text extraction
- Section detection
- Resume structure generation

---

## Job Description Module

Responsible for extracting:

- Required skills
- Preferred skills
- Important keywords
- Role information
- Company information
- Job responsibilities

---

## ATS Engine

Responsible for deterministic scoring.

---

## AI Service

Responsible for:

- Semantic matching
- Job-description interpretation
- Resume suggestions
- Experience rewrites
- Project rewrites

---

## Hallucination Guard

Responsible for detecting unsupported generated content.

---

## Optimization Engine

Responsible for building an improved resume using only safe changes.

---

## Score Protection Engine

Responsible for preventing ATS regression.

---

## Export Engine

Responsible for:

- PDF generation
- DOCX generation
- Adaptive resume layout

---

# 📊 ATS Scoring Engine

Rabbit AI uses a deterministic weighted scoring model.

| Component | Weight |
|---|---:|
| Skills Match | 25% |
| Keyword Match | 20% |
| Experience Relevance | 15% |
| Projects Relevance | 10% |
| Education Fit | 5% |
| Structure | 10% |
| Formatting | 5% |
| Quantification | 10% |
| **Total** | **100%** |

---

## ATS Formula

```text
ATS Score =

Skills Match          × 0.25
+
Keyword Match         × 0.20
+
Experience Relevance  × 0.15
+
Projects Relevance    × 0.10
+
Education Fit         × 0.05
+
Structure             × 0.10
+
Formatting            × 0.05
+
Quantification        × 0.10
```

The result is normalized to:

```text
0 – 100
```

---

# 🧮 ATS Component Explanation

## Skills Match — 25%

Measures how many job-relevant skills appear in the candidate resume.

Example:

```text
Job Skills

AWS
EC2
S3
Docker
Terraform
Linux
```

Resume contains:

```text
AWS
EC2
S3
```

Rabbit AI detects:

```text
Matched:
AWS
EC2
S3

Missing:
Docker
Terraform
Linux
```

---

## Keyword Match — 20%

Checks important role-specific terms extracted from the job description.

---

## Experience Relevance — 15%

Measures whether work experience demonstrates technologies or responsibilities relevant to the target role.

---

## Projects Relevance — 10%

Checks whether candidate projects provide evidence for the skills required by the job.

---

## Education Fit — 5%

Checks whether the resume contains usable education information.

---

## Structure — 10%

Checks important resume sections such as:

```text
Summary / Objective
Skills
Experience
Projects
Education
```

---

## Formatting — 5%

Evaluates basic ATS-readability indicators such as:

- Contact information
- Resume length
- Excessively long lines
- General text structure

---

## Quantification — 10%

Rewards measurable outcomes such as:

```text
Built 4 ML models

Processed 50,000+ records

Achieved 94% accuracy

Managed 500+ code snippets

Improved productivity by 30%

Solved 700+ DSA problems
```

---

# 🧠 Hybrid AI Architecture

Rabbit AI uses two different intelligence layers.

```text
               RESUME + JOB DESCRIPTION
                         │
             ┌───────────┴───────────┐
             │                       │
             ▼                       ▼
    Deterministic Engine       Semantic AI Engine
             │                       │
             ▼                       ▼
       ATS Compatibility        Meaning / Context
             │                       │
             └───────────┬───────────┘
                         ▼
                   Hybrid Analysis
```

---

## Why Hybrid?

A pure LLM score may change between runs.

A pure keyword engine may miss semantic relationships.

Rabbit AI combines:

```text
Deterministic Scoring
        +
Semantic Understanding
        =
More Explainable Analysis
```

---

# 📝 Job Description Intelligence

The job description is converted into structured information.

Example:

```text
Raw Job Description
       ↓
JobDescriptionAnalysis
       │
       ├── Company
       ├── Role
       ├── Required Skills
       ├── Preferred Skills
       └── Keywords
```

This structured representation is then used by the ATS engine.

---

# ⚡ Resume Optimization Engine

Optimization is performed in several stages.

```text
Original Resume
       ↓
Analyze Missing / Weak Areas
       ↓
Generate Safe Rewrite Candidates
       ↓
Hallucination Verification
       ↓
Build Optimized Structured Resume
       ↓
Convert Back To Resume Text
       ↓
Run ATS Engine Again
       ↓
Compare Scores
       ↓
Accept / Rollback
```

---

# 🛡️ Hallucination Guard

One of Rabbit AI's core design principles is:

> Never improve an ATS score by inventing candidate qualifications.

Example:

Job requires:

```text
AWS
Docker
Terraform
Kubernetes
Jenkins
```

Original resume contains:

```text
AWS
```

Rabbit AI can:

```text
✅ Better highlight AWS experience
✅ Rewrite existing AWS-related evidence
✅ Show missing Docker
✅ Show missing Terraform
✅ Show missing Kubernetes
✅ Show missing Jenkins
```

Rabbit AI should not:

```text
❌ Claim Docker experience
❌ Claim Terraform experience
❌ Create Kubernetes projects
❌ Add fake Jenkins pipelines
```

---

# 🚫 Rejected AI Rewrites

If AI proposes an unsupported experience or project rewrite, Rabbit AI can preserve it separately as:

```text
Rejected Experience Rewrites
Rejected Project Rewrites
```

This makes optimization explainable instead of silently trusting model output.

---

# 🔒 ATS Score Protection

AI rewriting does not automatically mean improvement.

Rabbit AI therefore evaluates every optimization candidate.

```text
Original ATS Score = 68
        ↓
AI Candidate Generated
        ↓
Candidate ATS Score = 65
        ↓
Candidate Rejected
        ↓
Original Score Protected
```

Another example:

```text
Original = 68
Candidate = 73
        ↓
Candidate Accepted
```

The core rule is:

```text
Optimized Score >= Original Score
```

before the candidate is considered stronger.

---

# 📈 Before vs After Match Report

Rabbit AI displays individual score changes.

Example:

```text
Overall Score
68 → 74

Skills Match
55 → 65

Keyword Match
57 → 70

Experience Relevance
60 → 67

Projects Relevance
70 → 75

Education Fit
100 → 100

Structure
100 → 100

Formatting
95 → 95

Quantification
80 → 90
```

It can also explain:

```text
Weakest ATS Area

Biggest Component Improvement

Weighted ATS Calculation

Skills To Highlight

Missing Skills

Rejected Unsafe Rewrites
```

---

# 📄 Structured Resume Model

Internally, Rabbit AI does not treat a resume as only plain text.

It builds a structured object.

```text
StructuredResume
│
├── name
├── email
├── phone
├── location
├── links
│
├── careerObjective
│
├── education[]
│   ├── institution
│   ├── duration
│   ├── qualification
│   ├── score
│   └── location
│
├── experience[]
│   ├── role
│   ├── company
│   ├── duration
│   └── bullets[]
│
├── skillCategories[]
│   ├── name
│   └── skills[]
│
├── projects[]
│   ├── name
│   ├── technologies
│   └── bullets[]
│
├── researchPublications[]
├── certifications[]
└── achievements[]
```

This improves:

- Section-level analysis
- Safer AI rewriting
- Professional export
- Version management

---

# 📖 Resume Parsing

## PDF Parsing

PDF resumes are processed using:

```text
Apache PDFBox
```

Flow:

```text
PDF
 ↓
PDFBox
 ↓
Extracted Text
 ↓
Resume Parser
 ↓
Structured Resume
```

---

## DOCX Parsing

DOCX resumes are processed using:

```text
Apache POI
```

Flow:

```text
DOCX
 ↓
Apache POI
 ↓
Paragraph Extraction
 ↓
Resume Parser
 ↓
Structured Resume
```

---

# 🗂️ Resume Versioning

Different job applications often require different resume versions.

Rabbit AI supports this concept through versioning.

```text
Original Resume
     │
     ├── Version 1 → Amazon AWS Engineer
     │
     ├── Version 2 → Java Developer
     │
     ├── Version 3 → Backend Engineer
     │
     └── Version 4 → Cloud Engineer
```

This makes it possible to maintain one base resume with multiple role-specific variations.

---

# 📥 PDF and DOCX Export

Rabbit AI supports professional export after optimization.

---

## PDF Export Architecture

```text
Structured Resume
       ↓
LatexResumeTemplateService
       ↓
LaTeX Source
       ↓
pdflatex
       ↓
Professional Resume PDF
```

Features:

- ATS-safe
- Single-column design
- Clickable links
- Compact section layout
- Dynamic spacing
- Professional typography
- Content-aware density

---

## DOCX Export Architecture

```text
Structured Resume
       ↓
ProfessionalResumeRendererService
       ↓
Apache POI
       ↓
Professional DOCX
```

Features:

- Structured sections
- Adaptive spacing
- Clickable email
- Clickable LinkedIn
- Clickable GitHub
- Clickable LeetCode
- Clickable HackerRank
- Professional bullets

---

# 📐 Adaptive Resume Layout

Different resumes contain different amounts of information.

Using one fixed layout can cause:

```text
Too much empty space
```

or:

```text
Very small unreadable fonts
```

Rabbit AI therefore estimates resume content density.

```text
Resume Content
     ↓
Density Estimation
     ↓
┌────────────┬────────────┬────────────┐
│            │            │
▼            ▼            ▼
SPACIOUS    NORMAL       COMPACT
│            │            │
▼            ▼            ▼
Short       Medium       Heavy
Resume      Resume       Resume
```

---

## Spacious Layout

Used for shorter resumes.

Provides:

- Larger font
- More vertical spacing
- Balanced page usage

---

## Normal Layout

Used for average-sized resumes.

Provides:

- Balanced font size
- Balanced margins
- Balanced spacing

---

## Compact Layout

Used for content-heavy resumes.

Provides:

- Reduced whitespace
- Smaller margins
- Still-readable font
- Maximum useful page area

The system does not intentionally delete resume information simply to force a one-page layout.

---

# 🔐 Authentication and Security

Rabbit AI includes a complete authentication layer.

Features:

- User Registration
- User Login
- JWT Token Generation
- Protected REST APIs
- User-Specific Resume Access
- Session Expiry Handling
- Logout

Authentication flow:

```text
User
 ↓
Register / Login
 ↓
Spring Security
 ↓
Credential Validation
 ↓
JWT Generated
 ↓
Frontend Stores Token
 ↓
Authenticated API Requests
```

---

# 👤 User Isolation

Resume information should belong only to the authenticated owner.

Conceptually:

```text
User A
 ├── Resume A1
 └── Resume A2

User B
 ├── Resume B1
 └── Resume B2
```

Users should not access another user's resume history.

---

# 📊 Dashboard

The dashboard provides resume management features.

Users can:

- View previously uploaded resumes
- Select an existing resume
- Delete a resume
- Reuse a resume
- Perform new analysis
- Continue optimization
- View previous resume history

---

# 🛠️ Technology Stack

## Frontend

```text
React
TypeScript
Vite
CSS
Fetch API
```

---

## Backend

```text
Java
Spring Boot
Spring Security
REST APIs
JWT Authentication
Maven
```

---

## Database

```text
MySQL
```

---

## Artificial Intelligence

```text
Ollama
Qwen3:8b
```

---

## Document Processing

```text
Apache PDFBox
Apache POI
```

---

## Resume Generation

```text
LaTeX
pdflatex
Apache POI
```

---

## Cloud Architecture

```text
Amazon EC2
Amazon RDS
Amazon S3
Amazon CloudFront
Nginx
```

---

## Development Tools

```text
Spring Tool Suite
Visual Studio Code
Git
GitHub
Maven
npm
```

---

# 📁 Project Structure

```text
Rabbit-AI-Resume-Intelligence/
│
├── backend/
│   │
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/resumeiq/
│   │       │       │
│   │       │       ├── ats/
│   │       │       │   └── AtsEngine.java
│   │       │       │
│   │       │       ├── controller/
│   │       │       │
│   │       │       ├── dto/
│   │       │       │
│   │       │       ├── entity/
│   │       │       │
│   │       │       ├── repository/
│   │       │       │
│   │       │       ├── security/
│   │       │       │
│   │       │       ├── service/
│   │       │       │   ├── AiService
│   │       │       │   ├── OllamaAiService
│   │       │       │   ├── ResumeParser
│   │       │       │   ├── OptimizationService
│   │       │       │   ├── LatexResumeTemplateService
│   │       │       │   └── ProfessionalResumeRendererService
│   │       │       │
│   │       │       └── util/
│   │       │
│   │       └── resources/
│   │
│   └── pom.xml
│
├── frontend/
│   │
│   ├── src/
│   │   ├── App.tsx
│   │   ├── App.css
│   │   ├── AuthGate.tsx
│   │   ├── AuthGate.css
│   │   ├── DashboardPanel.tsx
│   │   ├── Dashboard.css
│   │   ├── ProfilePanel.tsx
│   │   ├── Profile.css
│   │   ├── api.ts
│   │   ├── types.ts
│   │   └── main.tsx
│   │
│   ├── package.json
│   ├── package-lock.json
│   └── vite.config.ts
│
└── README.md
```

---

# 🔌 API Workflow

The frontend communicates with the Spring Boot backend through REST APIs.

Typical flow:

```text
React
 ↓
HTTP Request
 ↓
Spring Controller
 ↓
Service Layer
 ↓
Repository / AI / ATS Engine
 ↓
Response DTO
 ↓
React UI
```

---

## Main API Categories

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
```

---

### Resume Management

Conceptually includes operations for:

```text
Upload Resume
Get Resume
Delete Resume
List User Resumes
```

---

### ATS Analysis

```text
Resume ID
Company
Role
Job Description
      ↓
ATS Analysis API
      ↓
Analysis Result
```

---

### Resume Optimization

```text
Resume ID
Company
Role
Job Description
      ↓
Optimization API
      ↓
Optimized Version
```

---

### Export

```text
Optimized Version
       ↓
PDF Export

Optimized Version
       ↓
DOCX Export
```

---

# 🗄️ Database Design

Rabbit AI uses MySQL for persistent storage.

Core data concepts include:

```text
User
│
├── Authentication Information
│
└── Resume Ownership

Resume
│
├── Original File Information
├── Extracted Resume Text
└── User Reference

Resume Version
│
├── Structured Resume
├── Optimized Resume
├── Target Role
├── Target Company
└── Version Number

Analysis
│
├── ATS Score
├── Semantic Score
├── Skills
├── Keywords
└── Recommendations
```

Exact database entities may evolve as the project grows.

---

# ⚙️ Local Setup

## Prerequisites

Install:

```text
Java 17+
Maven
Node.js
npm
MySQL
Ollama
pdflatex / LaTeX Distribution
Git
```

---

# 🗄️ MySQL Setup

Create a database:

```sql
CREATE DATABASE rabbitai;
```

Configure the Spring datasource.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rabbitai
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Do not commit real database passwords.

---

# 🤖 Ollama Setup

Install Ollama and verify:

```bash
ollama --version
```

Pull the model:

```bash
ollama pull qwen3:8b
```

Check installed models:

```bash
ollama list
```

Run Qwen:

```bash
ollama run qwen3:8b
```

The Spring Boot AI service communicates with the Ollama runtime.

---

# ☕ Run Spring Boot Backend

Open the backend project.

Using Maven:

```bash
mvn spring-boot:run
```

Or using Spring Tool Suite:

```text
Project
→ Right Click
→ Run As
→ Spring Boot App
```

Backend default:

```text
http://localhost:8080
```

---

# ⚛️ Run React Frontend

Open the frontend folder:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start development server:

```bash
npm run dev
```

Frontend:

```text
http://localhost:5173
```

---

# 🧪 Typical Local Development Architecture

```text
Browser
  │
  ▼
React :5173
  │
  ▼
Spring Boot :8080
  │
  ├──── Ollama :11434
  │
  └──── MySQL :3306
```

---

# ☁️ AWS Deployment Architecture

Rabbit AI is designed for deployment using AWS services.

```text
                              INTERNET
                                 │
                                 ▼
                         Amazon CloudFront
                          HTTPS + CDN
                           /       \
                          /         \
                         ▼           ▼
                  React Frontend    /api/*
                    Amazon S3          │
                                      ▼
                                    Nginx
                                      │
                                      ▼
                                Amazon EC2
                                      │
                 ┌────────────────────┼────────────────────┐
                 │                    │                    │
                 ▼                    ▼                    ▼
            Spring Boot            Ollama              TeX Live
               :8080                :11434              pdflatex
                                      │
                                   Qwen3:8b
                 │
                 ▼
             Amazon RDS
               MySQL
               :3306
```

---

# AWS Component Responsibilities

## Amazon S3

Stores the production React static build.

```text
index.html
JavaScript bundles
CSS files
Static assets
```

---

## Amazon CloudFront

Provides:

- CDN
- HTTPS
- Frontend distribution
- Caching
- Public application entry point

---

## Amazon EC2

Runs backend services such as:

```text
Nginx
Spring Boot
Ollama
Qwen3:8b
TeX Live
```

---

## Amazon RDS

Hosts production MySQL data.

The database should not be publicly accessible.

---

## Nginx

Acts as reverse proxy.

```text
/api/*
   ↓
Nginx
   ↓
Spring Boot :8080
```

---

# 🔐 Production Security Design

Recommended inbound architecture:

| Port | Service | Access |
|---|---|---|
| 22 | SSH | Developer IP only |
| 80 | HTTP | Public |
| 443 | HTTPS | Public |
| 8080 | Spring Boot | Internal |
| 11434 | Ollama | Internal |
| 3306 | MySQL | EC2 security group only |

Never directly expose:

```text
Spring Boot :8080
Ollama      :11434
MySQL       :3306
```

to the public internet.

---

# 🔑 Production Environment Variables

Secrets should be configured through environment variables.

Example:

```text
DB_URL
DB_USERNAME
DB_PASSWORD

JWT_SECRET

OLLAMA_BASE_URL
OLLAMA_MODEL
```

Do not hard-code production credentials.

---

# 🧪 Example Use Case

Consider a candidate applying to:

```text
Company:
Amazon

Role:
AWS Cloud Engineer
```

Job description may require:

```text
AWS
EC2
S3
Lambda
RDS
IAM
VPC
CloudWatch
CloudFormation
Docker
Terraform
Jenkins
CI/CD
Linux
```

Candidate resume may already contain:

```text
AWS
Amazon EC2
Amazon S3
AWS Lambda
Amazon RDS
Git
GitHub
```

Rabbit AI may output:

```text
MATCHED / RELEVANT

AWS
Amazon EC2
Amazon S3
AWS Lambda
Amazon RDS
Git
GitHub
```

and:

```text
MISSING / NOT ADDED

IAM
Amazon VPC
CloudWatch
CloudFormation
Docker
Terraform
Jenkins
CI/CD
Linux
```

The optimizer will highlight supported skills but will not intentionally claim that the candidate has experience with missing technologies.

---

# 📋 Example Match Report

```text
Rabbit AI Match Report

ATS Score
68 → 68

Skills Match
55 → 55

Keyword Match
57 → 57

Experience Relevance
...

Projects Relevance
...

Education Fit
...

Structure
...

Formatting
...

Quantification
...
```

When the final score does not increase, Rabbit AI can explain why.

For example:

```text
The ATS score remained protected because
no evidence-safe optimization produced a
candidate stronger than the original resume.
```

---

# 🧪 Testing Strategy

Important workflows to test include:

## Resume Upload Tests

```text
Valid PDF
Valid DOCX
Empty document
Unsupported file
Large file
```

---

## Authentication Tests

```text
Register
Login
Invalid Password
Expired JWT
Unauthorized Request
```

---

## ATS Tests

```text
Strong Skill Match
Weak Skill Match
Missing Skills
No Experience
No Projects
No Education
Quantified Resume
Non-Quantified Resume
```

---

## AI Safety Tests

Check whether the AI attempts to introduce:

```text
Unknown Skills
Unknown Companies
Fake Metrics
Fake Experience
Fake Projects
```

Such unsupported changes should not be accepted.

---

## Export Tests

```text
PDF Generation
DOCX Generation
Clickable Links
One-Page Resume
Two-Page Resume
Short Resume
Heavy Resume
Special Characters
```

---

# 💡 Important Design Decisions

## 1. Deterministic ATS Score

ATS scoring is implemented in Java rather than asking the LLM to invent a score.

Why?

```text
More Consistent
More Explainable
More Testable
Less Random
```

---

## 2. AI Is Used For Language Understanding

AI is best used for:

```text
Semantic matching
Job-description understanding
Resume wording
Context analysis
```

not as the only scoring mechanism.

---

## 3. Original Resume Is Evidence

Optimization should be grounded in original candidate information.

---

## 4. Optimized Resume Is Re-Scored

The system does not assume an AI rewrite is automatically better.

---

## 5. Export Is Structured

Resume generation uses structured resume objects rather than simply dumping AI text into a file.

---

# ⚠️ Current Limitations

Rabbit AI is an engineering project and not an official ATS used by employers.

The ATS score should be interpreted as:

> A predictive resume-to-job compatibility score generated using Rabbit AI's scoring model.

It is not guaranteed to reproduce the private scoring algorithms used by individual employers or commercial ATS providers.

Other limitations may include:

- Skill catalog coverage
- Complex multi-column resume parsing
- Unusual PDF layouts
- Semantic model performance
- Hardware-dependent Ollama speed
- Job description quality
- Resume extraction inconsistencies

---

# 🚧 Why ATS Scores May Not Always Increase

A strong optimization system should not force a higher score at any cost.

Example:

```text
Job requires Kubernetes.

Resume has no Kubernetes evidence.
```

Rabbit AI should not add Kubernetes simply to increase the skills-match score.

Therefore:

```text
Missing Skill
     ↓
No Supporting Evidence
     ↓
Do Not Add
     ↓
Score May Remain Unchanged
```

This is expected behavior and protects resume integrity.

---

# 🔮 Future Enhancements

## AI and Matching

- Embedding-based semantic search
- Resume-to-job ranking
- Multi-model AI support
- Fine-tuned resume optimization model
- Improved skill taxonomy
- Experience-level prediction

---

## Career Intelligence

- Job recommendations
- Skill-gap learning roadmap
- Career path suggestions
- Interview preparation
- Interview question generation
- Cover letter generation

---

## Resume Builder

- Multiple resume templates
- Template customization
- Section reordering
- Theme selection
- Live resume editor
- Resume preview

---

## Recruiter Features

- Candidate ranking
- Recruiter dashboard
- Job creation
- Resume search
- Candidate comparison

---

## DevOps

- Docker
- Docker Compose
- GitHub Actions
- CI/CD
- Amazon CloudWatch
- Automated deployment
- Infrastructure as Code
- Terraform
- AWS CloudFormation

---

## Analytics

- ATS score history
- Application tracking
- Role-wise performance
- Skill gap trends
- Resume improvement analytics

---

# 🔐 Security Guidelines

Never commit secrets such as:

```text
Database Passwords
JWT Secret Keys
AWS Access Keys
AWS Secret Keys
SSH Private Keys
.pem Files
Production Environment Files
```

Recommended:

```text
Environment Variables
AWS Secrets Manager
AWS IAM Roles
Security Groups
Private RDS
HTTPS
```

---

# 📌 Recommended `.gitignore`

```gitignore
# ==============================
# Java / Maven
# ==============================

target/
*.class


# ==============================
# Node / React
# ==============================

node_modules/
dist/


# ==============================
# Environment
# ==============================

.env
.env.*
!.env.example


# ==============================
# Eclipse / STS
# ==============================

.metadata/
.settings/
.project
.classpath


# ==============================
# IntelliJ
# ==============================

.idea/


# ==============================
# VS Code
# ==============================

.vscode/


# ==============================
# Logs
# ==============================

*.log
logs/


# ==============================
# Operating System
# ==============================

.DS_Store
Thumbs.db


# ==============================
# Secrets
# ==============================

*.pem
*.key
*.p12
*.jks


# ==============================
# LaTeX Temporary Files
# ==============================

*.aux
*.out
*.toc


# ==============================
# Temporary Files
# ==============================

tmp/
temp/
```

---

# 📸 Screenshots

Add project screenshots here after uploading them to a GitHub folder such as:

```text
docs/screenshots/
```

Recommended screenshots:

```text
01-login.png
02-dashboard.png
03-ats-analysis.png
04-role-based-builder.png
05-match-report.png
06-optimized-resume.png
07-pdf-export.png
08-docx-export.png
```

Example README syntax:

```markdown
![Rabbit AI Dashboard](docs/screenshots/dashboard.png)
```

---

# 🧭 Suggested GitHub Screenshot Order

```text
Login
 ↓
Dashboard
 ↓
Resume Upload
 ↓
ATS Analyzer
 ↓
ATS Score Breakdown
 ↓
Role-Based Builder
 ↓
Before vs After Score
 ↓
Optimized Resume
 ↓
PDF Export
 ↓
DOCX Export
```

---

# 📚 Engineering Concepts Demonstrated

This project demonstrates practical implementation of:

```text
Full-Stack Development

REST API Design

Spring Boot Architecture

React State Management

JWT Authentication

Database Persistence

Document Parsing

AI Integration

Prompt Engineering

Hybrid AI Systems

Deterministic Scoring

LLM Safety Validation

Algorithm Design

Resume Versioning

PDF Generation

DOCX Generation

AWS Architecture

Application Security
```

---

# 🎯 Project Goals

Rabbit AI Resume Intelligence aims to answer several practical questions for a job seeker:

```text
How well does my resume match this specific job?

Why is my ATS compatibility score low?

Which skills already match?

Which required skills are missing?

Which experience is relevant?

Which projects support this job?

Is my resume properly structured?

Does my resume contain measurable achievements?

What can safely be improved?

What should not be added because I lack evidence?

Did optimization actually improve the resume?

Can I download a professional ATS-friendly version?
```

---

# 🌟 What Makes Rabbit AI Different?

```text
Traditional Resume Optimizer
          │
          ▼
     Send Resume To AI
          │
          ▼
      Rewrite Resume
```

Rabbit AI:

```text
Resume
  ↓
Parse
  ↓
Deterministic ATS Engine
  ↓
Semantic AI Analysis
  ↓
Evidence Verification
  ↓
Safe Optimization
  ↓
Hallucination Guard
  ↓
ATS Re-Scoring
  ↓
Score Protection
  ↓
Professional Export
```

The goal is not simply:

```text
"Make the resume sound better."
```

The goal is:

```text
"Make the resume more relevant,
more explainable,
more ATS-aware,
and more truthful."
```

---

# 🐇 Rabbit AI Resume Intelligence

### Analyze Smarter. Match Better. Optimize With Evidence.

Built using:

**Java · Spring Boot · React · TypeScript · MySQL · Ollama · Qwen3:8b · Apache PDFBox · Apache POI · LaTeX · AWS**

---

## ⭐ If you find this project useful

Consider giving the repository a star.

Contributions, ideas, and feedback are welcome.
