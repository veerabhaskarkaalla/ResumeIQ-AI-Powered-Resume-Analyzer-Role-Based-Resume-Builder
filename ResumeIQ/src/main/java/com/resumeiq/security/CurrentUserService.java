package com.resumeiq.security;

import org.springframework.http.HttpStatus;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import com.resumeiq.entity.User;
import com.resumeiq.repository.UserRepository;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;


    public CurrentUserService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(
                        authentication.getPrincipal()
                )) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required"
            );
        }


        String email =
                authentication.getName();


        return userRepository
                .findByEmail(
                        email.toLowerCase()
                )
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "Authenticated user not found"
                                )
                );
    }


    public Long getCurrentUserId() {

        return getCurrentUser()
                .getId();
    }
}