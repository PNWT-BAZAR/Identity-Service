package com.unsa.etf.Identity.Service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {
    public static final String topicExchangeName = "identity-to-orders-exchange";

    public static final String queueName = "identity-to-orders";

    @Bean
    Queue queue() {
        System.out.println("Created queue");
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.forward2.#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

//    @Bean(name = "pimAmqpAdmin")
//    public AmqpAdmin pimAmqpAdmin(@Qualifier("defaultConnectionFactory") ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(){
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(SPRING_RABBITMQ_HOST,SPRING_RABBITMQ_PORT);
//        connectionFactory.setUsername(SPRING_RABBITMQ_USERNAME);
//        connectionFactory.setPassword(SPRING_RABBITMQ_PASSWORD);
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setExchange("my.controller.exchange");
//        rabbitTemplate.setRoutingKey("my.controller.key");
//        return rabbitTemplate;
//    }
}
