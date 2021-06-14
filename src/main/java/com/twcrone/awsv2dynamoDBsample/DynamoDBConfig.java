package com.twcrone.awsv2dynamoDBsample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(
            @Value("${application.dynamodb.endpoint}") String dynamoDBEndpoint) {
        return DynamoDbAsyncClient.builder()
                .region(Region.US_ISO_EAST_1)
                .endpointOverride(URI.create(dynamoDBEndpoint))
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build();
    }
}