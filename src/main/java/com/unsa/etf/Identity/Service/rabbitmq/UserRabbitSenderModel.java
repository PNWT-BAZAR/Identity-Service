package com.unsa.etf.Identity.Service.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRabbitSenderModel {
    private OrderServiceUser user;
    private String operation;
}
