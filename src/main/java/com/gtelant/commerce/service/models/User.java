package com.gtelant.commerce.service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
//插建
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="last_name")
    private String lastName;

    @Column(name ="email", unique = true)
    private String email;

    @Column(name ="birthday")
    private LocalDate birthday;

    @Column(name ="address")
    private String address;

    @Column(name ="city")
    private String city;

    @Column(name ="segment")
    private String segment;

    @Column(name ="zipcode")
    private String zipcode;

    @Column(name ="password")
    private String password;

    @Column(name ="role")
    private String role;

    @Column(name ="has_subscribe")
    private boolean hasSubscribe;

    @Column(name ="last_seen_at")
    private String lastSeenAt;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="delete_at")
    private LocalDateTime deletedAt;
//表示一個 User可以有多個 UserSegment
    @OneToMany(mappedBy = "user")
    private List<UserSegment> userSegments;
}









