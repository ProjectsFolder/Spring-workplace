package com.example.demo.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@EnableRabbit
public class RabbitMqConfig {

    @Value("${rabbit.url}")
    private String url;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(URI.create(this.url));
    }

    @Bean
    public Queue queue() {
        var naming = new UUIDNamingStrategy();
        return new Queue(naming.generateName(), true, true, true);
    }

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange("test_topic.v1", false, false);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
