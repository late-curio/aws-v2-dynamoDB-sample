package com.twcrone.awsv2dynamoDBsample.externalTest;

import reactor.core.publisher.Mono;

public interface ExternalClientService {
    Mono<String> postPublicTests(String string);
}
