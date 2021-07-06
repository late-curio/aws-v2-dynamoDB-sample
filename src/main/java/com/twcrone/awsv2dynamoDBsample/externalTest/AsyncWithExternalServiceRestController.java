package com.twcrone.awsv2dynamoDBsample.externalTest;

import com.twcrone.awsv2dynamoDBsample.Customer;
import com.twcrone.awsv2dynamoDBsample.CustomerMapper;
import com.twcrone.awsv2dynamoDBsample.CustomerRepository;
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

        /*
        customer.setId(UUID.randomUUID().toString());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(customerTable)
                .item(CustomerMapper.toMap(customer))
                .build();

        return Mono.fromCompletionStage(client.putItem(putItemRequest))
                .map(putItemResponse -> putItemResponse.attributes())
                .map(attributeValueMap -> customer);

 */
        return Mono.just(Long.toString(System.currentTimeMillis()))
                .flatMap(externalClientService::postPublicTests) // --------------------(1) first external call
                .flatMap(t -> {
                    Customer customer = new Customer();
                    customer.setId(t);
                    customer.setName("Guff");
                    customer.setCity("Fortnite");
                    customer.setEmail("guff@fortnite.com");
                    final PutItemRequest request = PutItemRequest.builder()
                            .tableName("customers")
                            .item(CustomerMapper.toMap(customer))
                            .build();

                    return Mono.fromCompletionStage(() -> dynamoDbAsyncClient.putItem(request)) // -----------------(2) call to DynamoDb
                            .doOnError(e -> System.out.println("Failed to customer subscription"))
                            .thenReturn("saved");
                })
                .flatMap(externalClientService::postAnotherPublicTests); // ------------------------ (3) second external call
    }

    @GetMapping("/random")
    public String getRandom(@RequestParam(name = "seed") Long seed) {
        return Long.toString(new Random(seed).nextLong());
    }
}
