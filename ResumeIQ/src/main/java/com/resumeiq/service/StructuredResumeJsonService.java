package com.resumeiq.service;

import org.springframework.stereotype.Service;

import com.resumeiq.dto.StructuredResume;

import tools.jackson.databind.json.JsonMapper;

@Service
public class StructuredResumeJsonService {

    private final JsonMapper jsonMapper;


    public StructuredResumeJsonService(
            JsonMapper jsonMapper) {

        this.jsonMapper =
                jsonMapper;
    }


    public String toJson(
            StructuredResume resume) {

        if (resume == null) {

            return null;
        }


        try {

            return jsonMapper
                    .writeValueAsString(
                            resume
                    );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to serialize structured resume",
                    e
            );
        }
    }


    public StructuredResume fromJson(
            String json) {

        if (json == null
                || json.isBlank()) {

            return null;
        }


        try {

            return jsonMapper
                    .readValue(
                            json,
                            StructuredResume.class
                    );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to read structured resume",
                    e
            );
        }
    }
}