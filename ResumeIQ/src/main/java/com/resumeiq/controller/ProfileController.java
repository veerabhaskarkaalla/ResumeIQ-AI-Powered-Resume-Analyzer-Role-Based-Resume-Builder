package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.dto.ChangePasswordRequest;
import com.resumeiq.dto.UpdateProfileRequest;
import com.resumeiq.dto.UserProfileResponse;
import com.resumeiq.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {

    private final ProfileService
            profileService;


    public ProfileController(
            ProfileService profileService) {

        this.profileService =
                profileService;
    }


    @GetMapping
    public ResponseEntity<UserProfileResponse>
            getProfile() {

        return ResponseEntity.ok(
                profileService
                        .getProfile()
        );
    }


    @PutMapping
    public ResponseEntity<UserProfileResponse>
            updateProfile(
                    @RequestBody
                    UpdateProfileRequest request) {

        return ResponseEntity.ok(
                profileService
                        .updateProfile(request)
        );
    }


    @PutMapping("/password")
    public ResponseEntity<Void>
            changePassword(
                    @RequestBody
                    ChangePasswordRequest request) {

        profileService.changePassword(
                request
        );


        return ResponseEntity
                .noContent()
                .build();
    }
}