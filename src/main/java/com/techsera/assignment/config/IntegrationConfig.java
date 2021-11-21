package com.techsera.assignment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class IntegrationConfig {
    @Bean
    public MessageChannel receiverChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel replyChannel(){
        return new DirectChannel();
    }

    @Bean
    @Transformer(inputChannel = "router.channel.outOfStatus",outputChannel = "router.objectToJson.channel")
    public ObjectToJsonTransformer objectToJsonTransformer(){
        return new ObjectToJsonTransformer(getMapper());
    }

    @Bean
    public Jackson2JsonObjectMapper getMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        return new Jackson2JsonObjectMapper(objectMapper);
    }

    @ServiceActivator(inputChannel = "router.channel.getObject")
    @Bean
    public HeaderValueRouter headerValueRouter(){
        HeaderValueRouter headerValueRouter=new HeaderValueRouter("test");
        headerValueRouter.setChannelMapping("success","router.channel.outOfStatus");
        headerValueRouter.setChannelMapping("fail","router.channel.logInfo");
        return headerValueRouter;
    }

}
