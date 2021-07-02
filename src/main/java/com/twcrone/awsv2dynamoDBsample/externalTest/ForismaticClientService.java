package com.twcrone.awsv2dynamoDBsample.externalTest;

import com.twcrone.awsv2dynamoDBsample.externalTest.ExternalClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ForismaticClientService implements ExternalClientService {

    public Mono<String> postPublicTests(String key) {
        String uri = "http://api.forismatic.com/api/1.0/?method=getQuote&key=" + key + "&format=text&lang=en";
        return WebClient.create(uri)
                .get()
                .retrieve()
                .bodyToMono(String.class);
    }
}