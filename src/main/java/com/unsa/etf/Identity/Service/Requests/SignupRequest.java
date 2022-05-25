package com.unsa.etf.Identity.Service.Requests;

import com.unsa.etf.Identity.Service.Model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String shippingAddress;

    // TODO: 25.05.2022. for now
    private RoleEnum role;
}
