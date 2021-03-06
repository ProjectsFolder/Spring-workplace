package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqService.class);

    @RabbitListener(queues = "#{queue.name}")
    public void receive(String in) throws InterruptedException {
        log.info("Received from RabbitMQ {}", in);
    }
}
