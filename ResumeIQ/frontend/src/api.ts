import type {
  DashboardResponse,
  FinalAnalysisResult,
  OptimizationComparisonResult,
  Resume,
  UserProfileResponse,
} from "./types";


const API_BASE_URL =
  "http://localhost:8080";


function getToken(): string {
  return (
    localStorage.getItem(
      "resumeiq_token"
    ) ?? ""
  );
}


function authHeaders():
  Record<string, string> {

  return {
    Authorization:
      `Bearer ${getToken()}`,
  };
}


function handleUnauthorized(
  response: Response
) {

  if (
    response.status === 401 ||
    response.status === 403
  ) {

    localStorage.removeItem(
      "resumeiq_token"
    );


    window.dispatchEvent(
      new Event(
        "resumeiq-auth-expired"
      )
    );
  }
}


async function readError(
  response: Response
): Promise<string> {

  handleUnauthorized(
    response
  );


  try {

    const data =
      await response.json();


    return (
      data.message ??
      `Request failed: ${response.status}`
    );

  } catch {

    return (
      `Request failed: ${response.status}`
    );
  }
}


export async function uploadResume(
  file: File
): Promise<Resume> {

  const formData =
    new FormData();


  formData.append(
    "file",
    file
  );


  const response =
    await fetch(
      `${API_BASE_URL}/api/resumes/upload`,
      {
        method: "POST",

        headers: authHeaders(),

        body: formData,
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  return response.json();
}


export async function getResumeById(
  resumeId: number
): Promise<Resume> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/resumes/${resumeId}`,
      {
        method: "GET",

        headers: authHeaders(),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  return response.json();
}


export async function deleteResume(
  resumeId: number
): Promise<void> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/resumes/${resumeId}`,
      {
        method: "DELETE",

        headers: authHeaders(),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }
}


export async function analyzeResume(
  resumeId: number,
  company: string,
  role: string,
  jobDescription: string
): Promise<FinalAnalysisResult> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/analysis/final`,
      {
        method: "POST",

        headers: {
          ...authHeaders(),

          "Content-Type":
            "application/json",
        },

        body: JSON.stringify({
          resumeId,
          company,
          role,
          jobDescription,
        }),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  return response.json();
}


export async function optimizeResume(
  resumeId: number,
  company: string,
  role: string,
  jobDescription: string
): Promise<OptimizationComparisonResult> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/optimizer/optimize-and-score`,
      {
        method: "POST",

        headers: {
          ...authHeaders(),

          "Content-Type":
            "application/json",
        },

        body: JSON.stringify({
          resumeId,
          company,
          role,
          jobDescription,
        }),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  return response.json();
}


export async function getDashboard():
  Promise<DashboardResponse> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/dashboard`,
      {
        method: "GET",

        headers: authHeaders(),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  return response.json();
}


export async function getProfile():
  Promise<UserProfileResponse> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/profile`,
      {
        method: "GET",

        headers: authHeaders(),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  return response.json();
}


export async function updateProfile(
  name: string
): Promise<UserProfileResponse> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/profile`,
      {
        method: "PUT",

        headers: {
          ...authHeaders(),

          "Content-Type":
            "application/json",
        },

        body: JSON.stringify({
          name,
        }),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  return response.json();
}


export async function changePassword(
  currentPassword: string,
  newPassword: string
): Promise<void> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/profile/password`,
      {
        method: "PUT",

        headers: {
          ...authHeaders(),

          "Content-Type":
            "application/json",
        },

        body: JSON.stringify({
          currentPassword,
          newPassword,
        }),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }
}


export async function downloadResumeFile(
  versionId: number,
  type: "pdf" | "docx"
): Promise<void> {

  const response =
    await fetch(
      `${API_BASE_URL}/api/export/version/${versionId}/${type}`,
      {
        method: "GET",

        headers: authHeaders(),
      }
    );


  if (!response.ok) {

    throw new Error(
      await readError(response)
    );
  }


  const blob =
    await response.blob();


  const disposition =
    response.headers.get(
      "Content-Disposition"
    );


  let fileName =
    `ResumeIQ.${type}`;


  if (disposition) {

    const match =
      disposition.match(
        /filename="?([^"]+)"?/i
      );


    if (
      match &&
      match[1]
    ) {

      fileName =
        match[1];
    }
  }


  const objectUrl =
    URL.createObjectURL(
      blob
    );


  const link =
    document.createElement(
      "a"
    );


  link.href =
    objectUrl;


  link.download =
    fileName;


  document.body.appendChild(
    link
  );


  link.click();


  document.body.removeChild(
    link
  );


  URL.revokeObjectURL(
    objectUrl
  );
}