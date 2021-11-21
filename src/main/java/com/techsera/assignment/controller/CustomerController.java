package com.techsera.assignment.controller;

import com.techsera.assignment.entity.Customer;
import com.techsera.assignment.service.IntegrationGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;


@RestController
@RequestMapping("/integrate")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);


    @Autowired
    private IntegrationGateway integrationGateway;

    @PostMapping
    public void search(@RequestBody Customer customer){
        integrationGateway.process(customer);
    }
}
