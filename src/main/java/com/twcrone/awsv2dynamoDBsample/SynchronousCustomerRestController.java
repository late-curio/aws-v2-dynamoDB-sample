package com.twcrone.awsv2dynamoDBsample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SynchronousCustomerRestController {
    private final CustomerService customerService;

    public SynchronousCustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers/{id}/sync")
    Customer get(@PathVariable String id) {
        return this.customerService.getCustomerSync(id);
    }
}
