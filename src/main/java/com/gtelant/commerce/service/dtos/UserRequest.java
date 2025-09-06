package com.gtelant.commerce.service.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data

public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String address;
    private String city;
    private String zipcode;
    private String password;
    private String role;
    private boolean hasSubscribe;
}
