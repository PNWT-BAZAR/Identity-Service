package com.unsa.etf.Identity.Service.rabbitmq;

import com.unsa.etf.Identity.Service.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.unsa.etf.Identity.Service.rabbitmq.RabbitConfig.topicExchangeName;

@RequiredArgsConstructor
@Component
public class RabbitMessageSender {
    private final RabbitTemplate rabbitTemplate;

    public void notifyOrderServiceOfChange(User user, String operation){
        var orderServiceUser = mapIdentityUserToOrderUser(user);
        var rabbitUser = new UserRabbitSenderModel(orderServiceUser, operation);
        rabbitTemplate.convertAndSend(topicExchangeName, "foo.forward2.#", rabbitUser);
    }

    private OrderServiceUser mapIdentityUserToOrderUser (User user){
        return new OrderServiceUser(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getShippingAddress() );
    }
}
