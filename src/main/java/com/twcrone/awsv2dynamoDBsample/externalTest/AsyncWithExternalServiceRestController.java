package com.twcrone.awsv2dynamoDBsample.externalTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
public class AsyncWithExternalServiceRestController {
    private final ExternalClientService externalClientService;
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public AsyncWithExternalServiceRestController(DynamoDbAsyncClient dynamoDbAsyncClient,
                                                  ExternalClientService externalClientService) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        this.externalClientService = externalClientService;
    }

    @GetMapping("/forismatic")
    public Mono<String> getQuote() {
        return Mono.just(Long.toString(System.currentTimeMillis()))
                .flatMap(externalClientService::postPublicTests) // --------------------(1) first external call
                .flatMap(externalClientService::postAnotherPublicTests); // ------------------------ (2) second external call
    }

    @GetMapping("/dynamodb")
    public Mono<String> callWithDynamoDbCall() {
        final Map<String, AttributeValue> attributeValueMap = Map.of(
                "SubscriberId", AttributeValue.builder().s("value").build(),
                "NamespacePublisherId", AttributeValue.builder().s("value2").build());

        return Mono.just(Long.toString(System.currentTimeMillis()))
                .flatMap(externalClientService::postPublicTests) // --------------------(1) first external call
                .flatMap(t -> {
                    final PutItemRequest request = PutItemRequest.builder()
                            .tableName("presence-nudged-subscription-isolated")
                            .item(attributeValueMap)
                            .build();

                    return Mono.fromCompletionStage(() -> dynamoDbAsyncClient.putItem(request)) // -----------------(2) call to DynamoDb
                            .doOnError(e -> System.out.println("Failed to saveOrUpdate nudged subscription"))
                            .thenReturn("saved");
                })
                .flatMap(externalClientService::postAnotherPublicTests); // ------------------------ (3) second external call
    }

    @GetMapping("/random")
    public String getRandom(@RequestParam(name = "seed") Long seed) {
        return new Random(seed).toString();
    }
}
