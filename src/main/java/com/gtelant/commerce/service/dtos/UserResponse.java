package com.gtelant.commerce.service.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String address;
    private String city;
    private String zipcode;
    private String role;
    private boolean hasSubscribe;
    private LocalDateTime lastSeenAt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private List<UserSegmentResponse> userSegments;
}
