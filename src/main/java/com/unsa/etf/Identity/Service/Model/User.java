package com.unsa.etf.Identity.Service.Model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private String id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 10)
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20)
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 8, max = 15)
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String passwordHash;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "[+]?\\d{9,15}")
    private String phoneNumber;

    @Column(nullable = false)
    @NotBlank
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "roleId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;

    public User(String firstName, String lastName, String username, String passwordHash, String email, String phoneNumber, String shippingAddress, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.role = role;
    }
}
