import {
  useEffect,
  useState,
} from "react";

import App from "./App";

import "./AuthGate.css";


type Mode =
  | "login"
  | "register";


interface AuthResponse {
  token: string;
  userId: number;
  name: string;
  email: string;
}


const API_URL =
  "http://localhost:8080";


function AuthGate() {

  const [token, setToken] =
    useState<string | null>(
      localStorage.getItem(
        "resumeiq_token"
      )
    );


  const [mode, setMode] =
    useState<Mode>(
      "login"
    );


  const [name, setName] =
    useState("");


  const [email, setEmail] =
    useState("");


  const [password, setPassword] =
    useState("");


  const [error, setError] =
    useState("");


  const [loading, setLoading] =
    useState(false);


  useEffect(() => {

    function handleExpired() {

      localStorage.removeItem(
        "resumeiq_token"
      );

      setToken(null);
    }


    window.addEventListener(
      "resumeiq-auth-expired",
      handleExpired
    );


    return () => {

      window.removeEventListener(
        "resumeiq-auth-expired",
        handleExpired
      );
    };

  }, []);


  async function submit() {

    try {

      setError("");


      if (
        mode === "register" &&
        !name.trim()
      ) {

        setError(
          "Please enter your full name."
        );

        return;
      }


      if (!email.trim()) {

        setError(
          "Please enter your email."
        );

        return;
      }


      if (!password.trim()) {

        setError(
          "Please enter your password."
        );

        return;
      }


      setLoading(true);


      const endpoint =
        mode === "register"
          ? "/api/auth/register"
          : "/api/auth/login";


      const body =
        mode === "register"
          ? {
              name:
                name.trim(),

              email:
                email.trim(),

              password,
            }
          : {
              email:
                email.trim(),

              password,
            };


      const response =
        await fetch(
          API_URL + endpoint,
          {
            method: "POST",

            headers: {
              "Content-Type":
                "application/json",
            },

            body:
              JSON.stringify(
                body
              ),
          }
        );


      const data =
        await response.json();


      if (!response.ok) {

        throw new Error(
          data.message
            ?? "Authentication failed"
        );
      }


      const auth =
        data as AuthResponse;


      localStorage.setItem(
        "resumeiq_token",
        auth.token
      );


      localStorage.setItem(
        "resumeiq_user_name",
        auth.name
      );


      localStorage.setItem(
        "resumeiq_user_id",
        String(
          auth.userId
        )
      );


      setToken(
        auth.token
      );


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Authentication failed"
      );

    } finally {

      setLoading(false);
    }
  }


  function logout() {

    localStorage.removeItem(
      "resumeiq_token"
    );


    localStorage.removeItem(
      "resumeiq_user_name"
    );


    localStorage.removeItem(
      "resumeiq_user_id"
    );


    setToken(null);
  }


  if (token) {

    return (
      <>
        <button
          type="button"
          className="logout-button"
          onClick={
            logout
          }
        >
          Logout
        </button>

        <App />
      </>
    );
  }


  return (
    <div className="auth-page">

      <div className="auth-card">

        <div className="auth-brand">
          Rabbit AI Resume Intelligence
        </div>


        <p className="auth-subtitle">
          AI-Powered ATS Analysis,
          Job Matching & Resume Optimization
        </p>


        <div className="auth-tabs">

          <button
            type="button"
            className={
              mode === "login"
                ? "auth-tab active"
                : "auth-tab"
            }
            onClick={() => {

              setMode(
                "login"
              );

              setError("");
            }}
          >
            Login
          </button>


          <button
            type="button"
            className={
              mode === "register"
                ? "auth-tab active"
                : "auth-tab"
            }
            onClick={() => {

              setMode(
                "register"
              );

              setError("");
            }}
          >
            Register
          </button>

        </div>


        {mode === "register" && (

          <input
            className="auth-input"
            type="text"
            placeholder="Full name"
            value={
              name
            }
            onChange={(event) =>

              setName(
                event.target.value
              )
            }
          />

        )}


        <input
          className="auth-input"
          type="email"
          placeholder="Email"
          value={
            email
          }
          onChange={(event) =>

            setEmail(
              event.target.value
            )
          }
        />


        <input
          className="auth-input"
          type="password"
          placeholder="Password"
          value={
            password
          }
          onChange={(event) =>

            setPassword(
              event.target.value
            )
          }
          onKeyDown={(event) => {

            if (
              event.key === "Enter" &&
              !loading
            ) {

              void submit();
            }
          }}
        />


        {error && (

          <div className="auth-error">
            {error}
          </div>

        )}


        <button
          type="button"
          className="auth-submit"
          disabled={
            loading
          }
          onClick={
            submit
          }
        >

          {
            loading
              ? "Please wait..."
              : mode === "login"
              ? "Sign In"
              : "Create Account"
          }

        </button>


        <p className="auth-note">

          {
            mode === "login"
              ? "Sign in to analyze, match and optimize your resume with Rabbit AI."
              : "Create your account and build smarter, job-targeted resumes with Rabbit AI."
          }

        </p>

      </div>

    </div>
  );
}


export default AuthGate;