package com.resumeiq.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tools.jackson.databind.json.JsonMapper;

import com.resumeiq.dto.AiOptimizationCandidate;
import com.resumeiq.dto.OllamaGenerateRequest;
import com.resumeiq.dto.OllamaGenerateResponse;
import com.resumeiq.dto.OllamaOptions;
import com.resumeiq.dto.ResumeOptimizationRequest;
import com.resumeiq.dto.SemanticAnalysisRequest;
import com.resumeiq.dto.SemanticAnalysisResult;
import com.resumeiq.entity.Resume;
import com.resumeiq.service.ResumeService;

@Service
public class OllamaAiService implements AiService {

    private final ResumeService resumeService;

    private final PromptService promptService;

    private final JsonMapper jsonMapper;

    private final HttpClient httpClient;


    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;


    @Value("${ollama.model}")
    private String ollamaModel;


    public OllamaAiService(
            ResumeService resumeService,
            PromptService promptService,
            JsonMapper jsonMapper) {

        this.resumeService = resumeService;
        this.promptService = promptService;
        this.jsonMapper = jsonMapper;

        this.httpClient =
                HttpClient.newBuilder()
                        .connectTimeout(
                                Duration.ofSeconds(30)
                        )
                        .build();
    }


    @Override
    public SemanticAnalysisResult analyzeResume(
            SemanticAnalysisRequest request) {

        if (request == null
                || request.getResumeId() == null) {

            throw new IllegalArgumentException(
                    "Resume id is required"
            );
        }


        if (request.getJobDescription() == null
                || request.getJobDescription().isBlank()) {

            throw new IllegalArgumentException(
                    "Job description is required"
            );
        }


        Resume resume =
                resumeService.getResume(
                        request.getResumeId()
                );


        String prompt =
                promptService
                        .buildSemanticAnalysisPrompt(
                                resume.getExtractedText(),
                                request.getCompany(),
                                request.getRole(),
                                request.getJobDescription()
                        );


        String response =
                callOllama(
                        prompt,
                        createSemanticSchema()
                );


        try {

            SemanticAnalysisResult result =
                    jsonMapper.readValue(
                            response,
                            SemanticAnalysisResult.class
                    );


            if (result.getSemanticMatchScore() < 0) {
                result.setSemanticMatchScore(0);
            }

            if (result.getSemanticMatchScore() > 100) {
                result.setSemanticMatchScore(100);
            }


            if (result.getStrengths() == null) {
                result.setStrengths(List.of());
            }

            if (result.getGaps() == null) {
                result.setGaps(List.of());
            }

            if (result.getRelevantExperience() == null) {
                result.setRelevantExperience(List.of());
            }

            if (result.getRelevantProjects() == null) {
                result.setRelevantProjects(List.of());
            }

            if (result.getSuggestions() == null) {
                result.setSuggestions(List.of());
            }


            return result;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to parse semantic AI response: "
                    + response,
                    e
            );
        }
    }


    @Override
    public AiOptimizationCandidate generateOptimization(
            ResumeOptimizationRequest request) {

        if (request == null
                || request.getResumeId() == null) {

            throw new IllegalArgumentException(
                    "Resume id is required"
            );
        }


        if (request.getJobDescription() == null
                || request.getJobDescription().isBlank()) {

            throw new IllegalArgumentException(
                    "Job description is required"
            );
        }


        Resume resume =
                resumeService.getResume(
                        request.getResumeId()
                );


        String prompt =
                promptService.buildOptimizationPrompt(
                        resume.getExtractedText(),
                        request.getCompany(),
                        request.getRole(),
                        request.getJobDescription()
                );


        String response =
                callOllama(
                        prompt,
                        createOptimizationSchema()
                );


        try {

            AiOptimizationCandidate result =
                    jsonMapper.readValue(
                            response,
                            AiOptimizationCandidate.class
                    );


            if (result.getProfessionalSummary() == null) {
                result.setProfessionalSummary("");
            }

            if (result.getExperienceBullets() == null) {
                result.setExperienceBullets(List.of());
            }

            if (result.getProjectBullets() == null) {
                result.setProjectBullets(List.of());
            }

            if (result.getSkillsToHighlight() == null) {
                result.setSkillsToHighlight(List.of());
            }

            if (result.getMissingSkillsNotAdded() == null) {
                result.setMissingSkillsNotAdded(List.of());
            }

            if (result.getChanges() == null) {
                result.setChanges(List.of());
            }


            return result;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to parse optimization AI response: "
                    + response,
                    e
            );
        }
    }


    private String callOllama(
            String prompt,
            Object schema) {

        try {

            OllamaOptions options =
                    new OllamaOptions(
                            0.0,
                            1800
                    );


            OllamaGenerateRequest ollamaRequest =
                    new OllamaGenerateRequest(
                            ollamaModel,
                            prompt,
                            false,
                            schema,
                            false,
                            options
                    );


            String requestJson =
                    jsonMapper.writeValueAsString(
                            ollamaRequest
                    );


            HttpRequest httpRequest =
                    HttpRequest.newBuilder()

                            .uri(
                                    URI.create(
                                            ollamaBaseUrl
                                            + "/api/generate"
                                    )
                            )

                            .timeout(
                                    Duration.ofMinutes(5)
                            )

                            .header(
                                    "Content-Type",
                                    "application/json"
                            )

                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    requestJson
                                            )
                            )

                            .build();


            HttpResponse<String> httpResponse =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers
                                    .ofString()
                    );


            if (httpResponse.statusCode() < 200
                    || httpResponse.statusCode() >= 300) {

                throw new RuntimeException(
                        "Ollama HTTP "
                        + httpResponse.statusCode()
                        + ": "
                        + httpResponse.body()
                );
            }


            OllamaGenerateResponse ollamaResponse =
                    jsonMapper.readValue(
                            httpResponse.body(),
                            OllamaGenerateResponse.class
                    );


            if (ollamaResponse.getResponse() == null
                    || ollamaResponse
                            .getResponse()
                            .isBlank()) {

                throw new RuntimeException(
                        "Ollama returned empty response"
                );
            }


            return ollamaResponse.getResponse();

        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to communicate with Ollama: "
                    + e.getMessage(),
                    e
            );
        }
    }


    private Object createSemanticSchema() {

        Map<String, Object> properties =
                new LinkedHashMap<>();


        properties.put(
                "semanticMatchScore",
                Map.of(
                        "type", "integer",
                        "minimum", 0,
                        "maximum", 100
                )
        );


        properties.put(
                "summary",
                Map.of(
                        "type", "string"
                )
        );


        Object stringArray =
                stringArraySchema(5);


        properties.put(
                "strengths",
                stringArray
        );

        properties.put(
                "gaps",
                stringArray
        );

        properties.put(
                "relevantExperience",
                stringArray
        );

        properties.put(
                "relevantProjects",
                stringArray
        );

        properties.put(
                "suggestions",
                stringArray
        );


        return objectSchema(
                properties,
                List.of(
                        "semanticMatchScore",
                        "summary",
                        "strengths",
                        "gaps",
                        "relevantExperience",
                        "relevantProjects",
                        "suggestions"
                )
        );
    }


    private Object createOptimizationSchema() {

        Map<String, Object> properties =
                new LinkedHashMap<>();


        properties.put(
                "professionalSummary",
                Map.of(
                        "type", "string"
                )
        );


        properties.put(
                "experienceBullets",
                stringArraySchema(4)
        );


        properties.put(
                "projectBullets",
                stringArraySchema(4)
        );


        properties.put(
                "skillsToHighlight",
                stringArraySchema(8)
        );


        properties.put(
                "missingSkillsNotAdded",
                stringArraySchema(8)
        );


        properties.put(
                "changes",
                stringArraySchema(6)
        );


        return objectSchema(
                properties,
                List.of(
                        "professionalSummary",
                        "experienceBullets",
                        "projectBullets",
                        "skillsToHighlight",
                        "missingSkillsNotAdded",
                        "changes"
                )
        );
    }


    private Object stringArraySchema(
            int maxItems) {

        return Map.of(
                "type", "array",
                "items",
                Map.of(
                        "type", "string"
                ),
                "maxItems", maxItems
        );
    }


    private Object objectSchema(
            Map<String, Object> properties,
            List<String> required) {

        Map<String, Object> schema =
                new LinkedHashMap<>();


        schema.put(
                "type",
                "object"
        );


        schema.put(
                "properties",
                properties
        );


        schema.put(
                "required",
                required
        );


        schema.put(
                "additionalProperties",
                false
        );


        return schema;
    }
}