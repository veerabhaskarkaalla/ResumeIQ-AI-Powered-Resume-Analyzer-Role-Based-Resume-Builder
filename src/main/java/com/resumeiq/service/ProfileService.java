package com.resumeiq.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.resumeiq.dto.ChangePasswordRequest;
import com.resumeiq.dto.UpdateProfileRequest;
import com.resumeiq.dto.UserProfileResponse;
import com.resumeiq.entity.User;
import com.resumeiq.repository.UserRepository;
import com.resumeiq.security.CurrentUserService;

@Service
public class ProfileService {

    private final CurrentUserService
            currentUserService;

    private final UserRepository
            userRepository;

    private final PasswordEncoder
            passwordEncoder;


    public ProfileService(
            CurrentUserService currentUserService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.currentUserService =
                currentUserService;

        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;
    }


    public UserProfileResponse getProfile() {

        User user =
                currentUserService
                        .getCurrentUser();


        return mapProfile(
                user
        );
    }


    public UserProfileResponse updateProfile(
            UpdateProfileRequest request) {

        if (request == null
                || request.getName() == null
                || request.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Name is required"
            );
        }


        String name =
                request.getName()
                        .trim();


        if (name.length() < 2) {

            throw new IllegalArgumentException(
                    "Name must contain at least 2 characters"
            );
        }


        if (name.length() > 100) {

            throw new IllegalArgumentException(
                    "Name cannot exceed 100 characters"
            );
        }


        User user =
                currentUserService
                        .getCurrentUser();


        user.setName(
                name
        );


        User saved =
                userRepository.save(
                        user
                );


        return mapProfile(
                saved
        );
    }


    public void changePassword(
            ChangePasswordRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Password data is required"
            );
        }


        if (request.getCurrentPassword() == null
                || request
                    .getCurrentPassword()
                    .isBlank()) {

            throw new IllegalArgumentException(
                    "Current password is required"
            );
        }


        if (request.getNewPassword() == null
                || request
                    .getNewPassword()
                    .length() < 8) {

            throw new IllegalArgumentException(
                    "New password must contain at least 8 characters"
            );
        }


        User user =
                currentUserService
                        .getCurrentUser();


        boolean currentPasswordMatches =
                passwordEncoder.matches(
                        request.getCurrentPassword(),
                        user.getPasswordHash()
                );


        if (!currentPasswordMatches) {

            throw new IllegalArgumentException(
                    "Current password is incorrect"
            );
        }


        boolean samePassword =
                passwordEncoder.matches(
                        request.getNewPassword(),
                        user.getPasswordHash()
                );


        if (samePassword) {

            throw new IllegalArgumentException(
                    "New password must be different from current password"
            );
        }


        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );


        userRepository.save(
                user
        );
    }


    private UserProfileResponse mapProfile(
            User user) {

        UserProfileResponse response =
                new UserProfileResponse();


        response.setId(
                user.getId()
        );


        response.setName(
                user.getName()
        );


        response.setEmail(
                user.getEmail()
        );


        response.setCreatedAt(
                user.getCreatedAt()
        );


        return response;
    }
}