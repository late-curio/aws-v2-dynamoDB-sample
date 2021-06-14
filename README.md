# Getting Started
Modified version of *Spring Boot Webflux/Dynamo Tutorial* found here https://www.viralpatel.net/spring-boot-webflux-dynamodb/

Some code also stolen from here https://byegor.github.io/2020/02/21/spring-dynamodb-async.html

## Download DynamodDB Locally
https://s3.us-west-2.amazonaws.com/dynamodb-local/dynamodb_local_latest.tar.gz

## Run DynamoDB locally
`java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb`

OR use alternate method from here https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html

## Run tests
Creates 'customers' table in local DB if doesn't exist

`./gradlew test`

## Run app
Creates 'customers' table in local DB if doesn't exist

`./gradlew bootRun`

## Import Postman collection
`AWS v2 DynamoDB.postman_collection.json`



### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.1/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.1/gradle-plugin/reference/html/#build-image)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

