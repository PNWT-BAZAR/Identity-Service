package com.unsa.etf.Identity.Service.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table
public class User {
    @Id
    @GeneratedValue
    //@Column(nullable = false, updatable = false)
    private Integer id;

    //@Column(nullable = false)
    private String firstName;

    //@Column(nullable = false)
    private String lastName;

    //@Column(nullable = false, unique = true)
    private String username;

    //@Column(nullable = false)
    private String passwordHash;

    //@Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;
}
