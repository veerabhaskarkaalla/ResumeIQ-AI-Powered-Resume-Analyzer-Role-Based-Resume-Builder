import {
  useEffect,
  useState,
} from "react";


import {
  changePassword,
  getProfile,
  updateProfile,
} from "./api";


import type {
  UserProfileResponse,
} from "./types";


import "./Profile.css";


function ProfilePanel() {

  const [
    profile,
    setProfile,
  ] =
    useState<UserProfileResponse | null>(
      null
    );


  const [name, setName] =
    useState("");


  const [
    currentPassword,
    setCurrentPassword,
  ] =
    useState("");


  const [
    newPassword,
    setNewPassword,
  ] =
    useState("");


  const [
    confirmPassword,
    setConfirmPassword,
  ] =
    useState("");


  const [loading, setLoading] =
    useState(true);


  const [
    savingProfile,
    setSavingProfile,
  ] =
    useState(false);


  const [
    savingPassword,
    setSavingPassword,
  ] =
    useState(false);


  const [error, setError] =
    useState("");


  const [success, setSuccess] =
    useState("");


  async function loadProfile() {

    try {

      setLoading(true);

      setError("");


      const data =
        await getProfile();


      setProfile(
        data
      );


      setName(
        data.name
      );


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Unable to load profile"
      );

    } finally {

      setLoading(false);
    }
  }


  useEffect(() => {

    loadProfile();

  }, []);


  async function handleUpdateProfile() {

    if (!name.trim()) {

      setError(
        "Name is required."
      );

      return;
    }


    try {

      setError("");

      setSuccess("");

      setSavingProfile(true);


      const updated =
        await updateProfile(
          name.trim()
        );


      setProfile(
        updated
      );


      setName(
        updated.name
      );


      localStorage.setItem(
        "resumeiq_user_name",
        updated.name
      );


      setSuccess(
        "Profile updated successfully."
      );


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Profile update failed"
      );

    } finally {

      setSavingProfile(false);
    }
  }


  async function handleChangePassword() {

    if (!currentPassword) {

      setError(
        "Enter your current password."
      );

      return;
    }


    if (newPassword.length < 8) {

      setError(
        "New password must contain at least 8 characters."
      );

      return;
    }


    if (
      newPassword !==
      confirmPassword
    ) {

      setError(
        "New password and confirmation do not match."
      );

      return;
    }


    try {

      setError("");

      setSuccess("");

      setSavingPassword(true);


      await changePassword(
        currentPassword,
        newPassword
      );


      setCurrentPassword("");

      setNewPassword("");

      setConfirmPassword("");


      setSuccess(
        "Password changed successfully."
      );


    } catch (error) {

      setError(
        error instanceof Error
          ? error.message
          : "Password change failed"
      );

    } finally {

      setSavingPassword(false);
    }
  }


  if (loading) {

    return (
      <div className="profile-loading">
        Loading profile...
      </div>
    );
  }


  if (!profile) {

    return (
      <div className="error-box">
        {error || "Profile unavailable."}
      </div>
    );
  }


  return (
    <div className="profile-page">

      <div className="profile-heading">

        <h2>
          Account Profile
        </h2>

        <p>
          Manage your ResumeIQ account
          and security settings.
        </p>

      </div>


      {error && (

        <div className="profile-message error">
          {error}
        </div>

      )}


      {success && (

        <div className="profile-message success">
          {success}
        </div>

      )}


      <div className="profile-grid">

        <section className="profile-card">

          <div className="profile-card-title">

            <span>
              01
            </span>

            <h3>
              Personal Information
            </h3>

          </div>


          <div className="profile-avatar">

            {
              profile.name
                .charAt(0)
                .toUpperCase()
            }

          </div>


          <label className="profile-label">
            Full Name
          </label>


          <input
            className="profile-input"
            value={name}
            onChange={(event) => {

              setName(
                event.target.value
              );

              setError("");

              setSuccess("");
            }}
          />


          <label className="profile-label">
            Email
          </label>


          <input
            className="profile-input"
            value={profile.email}
            disabled
          />


          <p className="profile-help">
            Email cannot be changed
            in this version.
          </p>


          <div className="profile-meta">

            <span>
              User ID
            </span>

            <strong>
              #{profile.id}
            </strong>

          </div>


          <div className="profile-meta">

            <span>
              Member Since
            </span>

            <strong>
              {
                formatDate(
                  profile.createdAt
                )
              }
            </strong>

          </div>


          <button
            className="profile-primary-button"
            onClick={
              handleUpdateProfile
            }
            disabled={
              savingProfile
            }
          >

            {
              savingProfile
                ? "Saving..."
                : "Update Profile"
            }

          </button>

        </section>


        <section className="profile-card">

          <div className="profile-card-title">

            <span>
              02
            </span>

            <h3>
              Security
            </h3>

          </div>


          <h4>
            Change Password
          </h4>


          <p className="security-description">
            Use a different password
            from your current password.
          </p>


          <label className="profile-label">
            Current Password
          </label>


          <input
            className="profile-input"
            type="password"
            value={
              currentPassword
            }
            onChange={(event) => {

              setCurrentPassword(
                event.target.value
              );

              setError("");

              setSuccess("");
            }}
            placeholder="Current password"
          />


          <label className="profile-label">
            New Password
          </label>


          <input
            className="profile-input"
            type="password"
            value={
              newPassword
            }
            onChange={(event) => {

              setNewPassword(
                event.target.value
              );

              setError("");

              setSuccess("");
            }}
            placeholder="Minimum 8 characters"
          />


          <label className="profile-label">
            Confirm New Password
          </label>


          <input
            className="profile-input"
            type="password"
            value={
              confirmPassword
            }
            onChange={(event) => {

              setConfirmPassword(
                event.target.value
              );

              setError("");

              setSuccess("");
            }}
            placeholder="Repeat new password"
          />


          <button
            className="profile-primary-button"
            onClick={
              handleChangePassword
            }
            disabled={
              savingPassword
            }
          >

            {
              savingPassword
                ? "Changing..."
                : "Change Password"
            }

          </button>


          <div className="security-info">

            <strong>
              Security
            </strong>

            <p>
              Passwords are stored as
              BCrypt hashes and protected
              API requests require JWT
              authentication.
            </p>

          </div>

        </section>

      </div>

    </div>
  );
}


function formatDate(
  value: string
): string {

  if (!value) {
    return "—";
  }


  return new Date(
    value
  ).toLocaleDateString(
    undefined,
    {
      year: "numeric",
      month: "long",
      day: "numeric",
    }
  );
}


export default ProfilePanel;