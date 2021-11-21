package com.techsera.assignment.service;

import com.techsera.assignment.entity.Customer;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface IntegrationGateway {

    @Gateway(requestChannel = "router.channel")
    void process(Customer object);

}
