package com.twcrone.awsv2dynamoDBsample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Routes {

    private final CustomerService customerService;

    public Routes(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Bean
    RouterFunction<ServerResponse> customers() {
        return route(GET("/customers"), customerService::listCustomers)
                .andRoute(POST("/customers"), customerService::createCustomer)
                .andRoute(GET("/customers/{customerId}"), customerService::getCustomer)
                .andRoute(PUT("/customers/{customerId}"), customerService::updateCustomer)
                .andRoute(DELETE("/customers/{customerId}"), customerService::deleteCustomer);
    }
}