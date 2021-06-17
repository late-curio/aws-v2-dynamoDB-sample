package com.twcrone.awsv2dynamoDBsample;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
// snippet-end:[dynamoasyn.java2.get_item.import]

public class DynamoDBAsyncGetItem {

    public static void main(String[] args) {

//        final String USAGE = "\n" +
//                "Usage:\n" +
//                "    DynamoDBAsyncGetItem <table> <key> <keyVal>\n\n" +
//                "Where:\n" +
//                "    table - the table from which an item is retrieved (i.e., Music3)\n" +
//                "    key -  the key used in the table (i.e., Artist) \n" +
//                "    keyval  - the key value that represents the item to get (i.e., Famous Band)\n" +
//                " Example:\n" +
//                "    Music3 Artist Famous Band\n" +
//                "  **Warning** This program will actually retrieve an item\n" +
//                "            that you specify!\n";
//
//        if (args.length < 3) {
//            System.out.println(USAGE);
//            System.exit(1);
//        }

        String tableName = "customers";
        String key = "customerId";

        // TODO: Change to an ID in YOUR AWS "customers" db
        String keyVal = "ae04d82e-8210-4edd-9ab9-1dc9e2a1b8af";

        // TODO: Change to YOUR db region
        Region region = Region.US_EAST_1;
        DynamoDbAsyncClient client = DynamoDbAsyncClient.builder()
                .region(region)
                .build();

        System.out.format("Retrieving item \"%s\" from \"%s\"\n", keyVal, tableName );
        getItem(client, tableName, key, keyVal);
    }

    // snippet-start:[dynamoasyc.java2.get_item.main]
    public static void getItem(DynamoDbAsyncClient client, String tableName, String key,  String keyVal) {

        HashMap<String, AttributeValue> keyToGet =
                new HashMap<String, AttributeValue>();

        keyToGet.put(key, AttributeValue.builder()
                .s(keyVal).build());

        try {

            // Create a GetItemRequest instance
            GetItemRequest request = GetItemRequest.builder()
                    .key(keyToGet)
                    .tableName(tableName)
                    .build();

            // Invoke the DynamoDbAsyncClient object's getItem
            java.util.Collection<software.amazon.awssdk.services.dynamodb.model.AttributeValue> returnedItem = client.getItem(request).join().item().values();

            // Convert Set to Map
            Map<String, AttributeValue> map = returnedItem.stream().collect(Collectors.toMap(AttributeValue::s, s->s));
            Set<String> keys = map.keySet();
            for (String sinKey : keys) {
                System.out.format("%s: %s\n", sinKey, map.get(sinKey).toString());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        // snippet-end:[dynamoasyc.java2.get_item.main]
    }
}

// snippet-end:[dynamodb.Java.DynamoDBAsyncGetItem.complete]