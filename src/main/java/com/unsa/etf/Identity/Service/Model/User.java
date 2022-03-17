package com.unsa.etf.Identity.Service.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private String email;
    private String phoneNumber;
    private String shippingAddress;
    private Integer roleId;
}
