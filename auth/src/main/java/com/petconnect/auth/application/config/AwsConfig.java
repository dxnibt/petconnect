package com.petconnect.auth.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {

    @Value("${aws.access.key:}")
    private String awsAccessKey;

    @Value("${aws.secret.key:}")
    private String awsSecretKey;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Bean
    public SqsClient sqsClient() {
        // Si no hay credenciales, usa el provider por defecto (para local)
        if (awsAccessKey.isEmpty() || awsSecretKey.isEmpty()) {
            return SqsClient.builder()
                    .region(Region.of(awsRegion))
                    .build();
        }

        return SqsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(awsAccessKey, awsSecretKey)
                        )
                )
                .build();
    }
}