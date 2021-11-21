package com.techsera.assignment.service;

import com.techsera.assignment.constant.CustomerStatus;
import com.techsera.assignment.controller.CustomerController;
import com.techsera.assignment.entity.Customer;
import com.techsera.assignment.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class IntegrationService {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    CustomerRepository customerRepository;


    private Message<Customer> customMessage(Customer customer,String action,Message<Customer> message){
        Message<Customer> newMessage=MessageBuilder.withPayload(customer).setHeader("test",action).build();

        return newMessage;
    }

    @ServiceActivator(inputChannel = "router.channel", outputChannel ="router.channel.getObject")
    public Message<Customer> anotherMessage(Message<Customer> message) throws MessagingException{
        Optional<Customer> customer=customerRepository.findByNameAndPassword(message.getPayload().getName(),message.getPayload().getPassword());
        if(customer.isPresent()){
            if(customer.get().getCustomerStatus()== CustomerStatus.ACTIVE){
                return customMessage(customer.get(),"success",message);
            }
            return customMessage(customer.get(),"fail",message);
        }

        return customMessage(new Customer(message.getPayload().getName(),null,CustomerStatus.INACTIVE),"fail",message);
    }


    @ServiceActivator(inputChannel = "router.objectToJson.channel")
    public void anotherMessage2(Message<Customer> message) {


        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://jsonplaceholder.typicode.com/posts", HttpMethod.POST, new HttpEntity(message.getPayload(), new HttpHeaders()),String.class);
        log.info(response.toString());
    }

    @ServiceActivator(inputChannel = "router.channel.logInfo")
    public void anotherMessage3(Message<Customer> message) {

        log.info(message.getPayload().toString());
    }



}
