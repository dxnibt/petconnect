package com.petconnect.auth.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {

    @Bean
    public SqsClient sqsClient() {
        // Lee las credenciales desde variables de entorno
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        String region = System.getenv("AWS_REGION");

        return SqsClient.builder()
                .region(Region.of(region != null ? region : "us-east-1"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
<<<<<<< HEAD
                                AwsBasicCredentials.create(
                                        "[AWSACCESSKEY]",
                                        "[SECRETACCESSKEY]"
                                )
=======
                                AwsBasicCredentials.create(accessKey, secretKey)
>>>>>>> 9792caaa398d6fa8a5907924230055a76da4c622
                        )
                )
                .build();
    }
}
