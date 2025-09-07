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
//Lombok插建不用寫getter setter toString
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
    private LocalDateTime lastSeenAt;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="delete_at")
    private LocalDateTime deletedAt;
//表示一個 User可以有多個 UserSegment
//cascade = CascadeType.ALL 代表對 User 物件做的所有操作（新增、刪除、更新）都會自動套用到關聯的 UserSegment 物件。
//fetch = FetchType.LAZY 代表查詢 User 時，不會馬上載入 userSegments
// ，只有在真正用到 userSegments 時才會去資料庫抓資料（懶加載）,有關聯到其他表的可以使用。
//@JsonBackReference：父 ，避免無限遞迴
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserSegment> userSegments;
}









