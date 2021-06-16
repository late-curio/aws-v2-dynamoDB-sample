package com.twcrone.awsv2dynamoDBsample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository
public class CustomerRepository {

    public static final String ID_COLUMN = "customerId";
    private final DynamoDbAsyncClient client;
    private final DynamoDbClient syncClient;
    private final String customerTable;

    public CustomerRepository(DynamoDbAsyncClient client,
                              DynamoDbClient syncClient,
                              @Value("${application.dynamodb.customer_table}") String customerTable) {
        this.client = client;
        this.customerTable = customerTable;
        this.syncClient = syncClient;
    }

    public Flux<Customer> listCustomers() {

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(customerTable)
                .build();

        return Mono.fromCompletionStage(client.scan(scanRequest))
                .map(scanResponse -> scanResponse.items())
                .map(CustomerMapper::fromList)
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Customer> createCustomer(Customer customer) {

        customer.setId(UUID.randomUUID().toString());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(customerTable)
                .item(CustomerMapper.toMap(customer))
                .build();

        return Mono.fromCompletionStage(client.putItem(putItemRequest))
                .map(putItemResponse -> putItemResponse.attributes())
                .map(attributeValueMap -> customer);
    }

    public Mono<String> deleteCustomer(String customerId) {
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(customerTable)
                .key(Map.of("customerId", AttributeValue.builder().s(customerId).build()))
                .build();

        return Mono.fromCompletionStage(client.deleteItem(deleteItemRequest))
                .map(deleteItemResponse -> deleteItemResponse.attributes())
                .map(attributeValueMap -> customerId);
    }

    public Mono<Customer> getCustomer(String customerId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(customerTable)
                .key(Map.of("customerId", AttributeValue.builder().s(customerId).build()))
                .build();

        return Mono.fromCompletionStage(client.getItem(getItemRequest))
                .map(getItemResponse -> getItemResponse.item())
                .map(CustomerMapper::fromMap);
    }

    public Customer getCustomerSync(String customerId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(customerTable)
                .key(Map.of("customerId", AttributeValue.builder().s(customerId).build()))
                .build();

        return CustomerMapper.fromMap(syncClient.getItem(getItemRequest).item());
    }

    public Mono<String> updateCustomer(String customerId, Customer customer) {

        customer.setId(customerId);
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(customerTable)
                .item(CustomerMapper.toMap(customer))
                .build();

        return Mono.fromCompletionStage(client.putItem(putItemRequest))
                .map(updateItemResponse -> customerId);
    }

    //Creating table on startup if not exists
    @PostConstruct
    public void createTableIfNeeded() throws ExecutionException, InterruptedException {
        ListTablesRequest request = ListTablesRequest.builder().build();
        CompletableFuture<ListTablesResponse> listTableResponse = client.listTables(request);

        CompletableFuture<CreateTableResponse> createTableRequest = listTableResponse
                .thenCompose(response -> {
                    boolean tableExist = response.tableNames().contains(customerTable);
                    if (!tableExist) {
                        return createTable();
                    } else {
                        return CompletableFuture.completedFuture(null);
                    }
                });

        //Wait in synchronous manner for table creation
        createTableRequest.get();
    }

    private CompletableFuture<CreateTableResponse> createTable() {

        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(customerTable)

                .keySchema(KeySchemaElement.builder().attributeName(ID_COLUMN).keyType(KeyType.HASH).build())
                .attributeDefinitions(AttributeDefinition.builder().attributeName(ID_COLUMN).attributeType(ScalarAttributeType.S).build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();

        return client.createTable(request);
    }

}