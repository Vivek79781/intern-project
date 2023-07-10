package com.project.pagerduty.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Component
@Path("/hello")
public class HelloWorldController {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello, World!";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String hello(Map<String,Object> payload){
        logger.info(String.valueOf(taskExecutor.getActiveCount()));
        logger.info(payload.get("we").getClass().toString());
        return String.valueOf(taskExecutor.getActiveCount());
    }
}
