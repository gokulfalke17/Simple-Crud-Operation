package com.wedding.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.model.DbSecret01;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
@RequiredArgsConstructor
public class SecretsService {

    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper objectMapper;

    public DbSecret01 getDbSecret() throws Exception {

        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId("DbSecret-01")  // Your secret name
                .build();

        GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);

        // This will now ignore unknown fields like "engine"
        return objectMapper.readValue(response.secretString(), DbSecret01.class);
    }
}
