package com.resumeiq.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resumeiq.entity.User;

import tools.jackson.databind.json.JsonMapper;

@Service
public class JwtService {

    private final JsonMapper jsonMapper;


    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expiration-ms}")
    private long expirationMs;


    public JwtService(
            JsonMapper jsonMapper) {

        this.jsonMapper = jsonMapper;
    }


    public String generateToken(
            User user) {

        try {

            String headerJson =
                    """
                    {
                      "alg": "HS256",
                      "typ": "JWT"
                    }
                    """;


            long now =
                    Instant.now()
                            .getEpochSecond();


            long expiration =
                    now
                    + (expirationMs / 1000);


            Map<String, Object> payload =
                    new LinkedHashMap<>();


            payload.put(
                    "sub",
                    user.getEmail()
            );


            payload.put(
                    "uid",
                    user.getId()
            );


            payload.put(
                    "name",
                    user.getName()
            );


            payload.put(
                    "iat",
                    now
            );


            payload.put(
                    "exp",
                    expiration
            );


            String payloadJson =
                    jsonMapper
                            .writeValueAsString(
                                    payload
                            );


            String header =
                    encode(
                            headerJson
                                    .getBytes(
                                            StandardCharsets.UTF_8
                                    )
                    );


            String body =
                    encode(
                            payloadJson
                                    .getBytes(
                                            StandardCharsets.UTF_8
                                    )
                    );


            String unsignedToken =
                    header
                    + "."
                    + body;


            String signature =
                    createSignature(
                            unsignedToken
                    );


            return unsignedToken
                    + "."
                    + signature;


        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to generate JWT token",
                    e
            );
        }
    }


    public String extractEmail(
            String token) {

        Map<String, Object> claims =
                getClaims(token);


        Object subject =
                claims.get("sub");


        if (subject == null) {
            return null;
        }


        return subject.toString();
    }


    public boolean isTokenValid(
            String token) {

        try {

            String[] parts =
                    token.split("\\.");


            if (parts.length != 3) {
                return false;
            }


            String unsignedToken =
                    parts[0]
                    + "."
                    + parts[1];


            String expectedSignature =
                    createSignature(
                            unsignedToken
                    );


            boolean validSignature =
                    MessageDigest.isEqual(
                            expectedSignature
                                .getBytes(
                                    StandardCharsets.UTF_8
                                ),
                            parts[2]
                                .getBytes(
                                    StandardCharsets.UTF_8
                                )
                    );


            if (!validSignature) {
                return false;
            }


            Map<String, Object> claims =
                    getClaims(token);


            Object expirationValue =
                    claims.get("exp");


            if (!(expirationValue
                    instanceof Number)) {

                return false;
            }


            long expiration =
                    ((Number) expirationValue)
                            .longValue();


            long now =
                    Instant.now()
                            .getEpochSecond();


            return expiration > now;


        } catch (Exception e) {

            return false;
        }
    }


    @SuppressWarnings("unchecked")
    private Map<String, Object> getClaims(
            String token) {

        try {

            String[] parts =
                    token.split("\\.");


            if (parts.length != 3) {

                throw new RuntimeException(
                        "Invalid JWT token"
                );
            }


            byte[] decoded =
                    Base64.getUrlDecoder()
                            .decode(
                                    parts[1]
                            );


            String json =
                    new String(
                            decoded,
                            StandardCharsets.UTF_8
                    );


            return jsonMapper.readValue(
                    json,
                    Map.class
            );


        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid JWT token",
                    e
            );
        }
    }


    private String createSignature(
            String value)
            throws Exception {

        Mac mac =
                Mac.getInstance(
                        "HmacSHA256"
                );


        SecretKeySpec key =
                new SecretKeySpec(
                        secret.getBytes(
                                StandardCharsets.UTF_8
                        ),
                        "HmacSHA256"
                );


        mac.init(key);


        byte[] signature =
                mac.doFinal(
                        value.getBytes(
                                StandardCharsets.UTF_8
                        )
                );


        return encode(signature);
    }


    private String encode(
            byte[] bytes) {

        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        bytes
                );
    }
}