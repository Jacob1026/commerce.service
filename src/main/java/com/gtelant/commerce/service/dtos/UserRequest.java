package com.gtelant.commerce.service.dtos;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data

public class UserRequest {
    @NotNull(message = "不能空白")
    private String firstName;
    @NotNull(message = "不能空白")
    private String lastName;
    @NotNull(message = "不能空白")
    private String email;
    private LocalDate birthday;
    private String address;
    private String city;
    private String zipcode;
    @NotNull(message = "不能空白")
    private String password;
    private String role;
    private boolean hasSubscribe;
}
