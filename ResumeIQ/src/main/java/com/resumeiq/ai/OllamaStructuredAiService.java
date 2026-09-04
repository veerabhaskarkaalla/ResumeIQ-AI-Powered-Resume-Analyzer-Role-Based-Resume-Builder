package com.resumeiq.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resumeiq.dto.BulletOptimizationRequest;
import com.resumeiq.dto.BulletOptimizationResult;
import com.resumeiq.dto.OllamaGenerateRequest;
import com.resumeiq.dto.OllamaGenerateResponse;
import com.resumeiq.dto.OllamaOptions;

import tools.jackson.databind.json.JsonMapper;

@Service
public class OllamaStructuredAiService
        implements StructuredAiService {

    private final JsonMapper jsonMapper;

    private final StructuredPromptService
            promptService;

    private final HttpClient httpClient;


    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;


    @Value("${ollama.model}")
    private String ollamaModel;


    public OllamaStructuredAiService(
            JsonMapper jsonMapper,
            StructuredPromptService promptService) {

        this.jsonMapper =
                jsonMapper;

        this.promptService =
                promptService;

        this.httpClient =
                HttpClient
                        .newBuilder()
                        .connectTimeout(
                                Duration.ofSeconds(20)
                        )
                        .build();
    }


    @Override
    public BulletOptimizationResult
            optimizeBullets(
                    BulletOptimizationRequest request) {

        try {

            String prompt =
                    promptService
                            .buildBulletPrompt(
                                    request
                            );


            Map<String, Object> schema =
                    createSchema();


            OllamaOptions options =
                    new OllamaOptions(
                            0.0,
                            1200
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
                    jsonMapper
                            .writeValueAsString(
                                    ollamaRequest
                            );


            HttpRequest httpRequest =
                    HttpRequest
                            .newBuilder()
                            .uri(
                                    URI.create(
                                            ollamaBaseUrl
                                            + "/api/generate"
                                    )
                            )
                            .timeout(
                                    Duration.ofMinutes(4)
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
                            HttpResponse
                                .BodyHandlers
                                .ofString()
                    );


            if (httpResponse.statusCode()
                    < 200
                    ||
                httpResponse.statusCode()
                    >= 300) {

                throw new RuntimeException(
                        "Ollama returned HTTP "
                        + httpResponse.statusCode()
                );
            }


            OllamaGenerateResponse wrapper =
                    jsonMapper.readValue(
                            httpResponse.body(),
                            OllamaGenerateResponse.class
                    );


            if (wrapper == null
                    ||
                wrapper.getResponse() == null
                    ||
                wrapper
                    .getResponse()
                    .isBlank()) {

                throw new RuntimeException(
                        "Ollama returned an empty structured optimization response"
                );
            }


            BulletOptimizationResult result =
                    jsonMapper.readValue(
                            wrapper.getResponse(),
                            BulletOptimizationResult.class
                    );


            if (result.getRewrites()
                    == null) {

                result.setRewrites(
                        new ArrayList<>()
                );
            }


            return result;


        } catch (Exception e) {

            throw new RuntimeException(
                    "Structured resume optimization failed: "
                    + e.getMessage(),
                    e
            );
        }
    }


    private Map<String, Object> createSchema() {

        Map<String, Object> sourceIndex =
                new LinkedHashMap<>();


        sourceIndex.put(
                "type",
                "integer"
        );


        sourceIndex.put(
                "minimum",
                0
        );


        Map<String, Object> text =
                new LinkedHashMap<>();


        text.put(
                "type",
                "string"
        );


        Map<String, Object> rewriteProperties =
                new LinkedHashMap<>();


        rewriteProperties.put(
                "sourceIndex",
                sourceIndex
        );


        rewriteProperties.put(
                "text",
                text
        );


        Map<String, Object> rewriteItem =
                new LinkedHashMap<>();


        rewriteItem.put(
                "type",
                "object"
        );


        rewriteItem.put(
                "properties",
                rewriteProperties
        );


        rewriteItem.put(
                "required",
                List.of(
                        "sourceIndex",
                        "text"
                )
        );


        rewriteItem.put(
                "additionalProperties",
                false
        );


        Map<String, Object> rewrites =
                new LinkedHashMap<>();


        rewrites.put(
                "type",
                "array"
        );


        rewrites.put(
                "items",
                rewriteItem
        );


        Map<String, Object> properties =
                new LinkedHashMap<>();


        properties.put(
                "rewrites",
                rewrites
        );


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
                List.of(
                        "rewrites"
                )
        );


        schema.put(
                "additionalProperties",
                false
        );


        return schema;
    }
}