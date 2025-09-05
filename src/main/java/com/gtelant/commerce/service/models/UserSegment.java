package com.gtelant.commerce.service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_segments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //外來鍵，來自user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //外來鍵，來自segment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id")
    private Segment segment;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="deleted_at")
    private LocalDateTime deletedAt;
}
/*

複合主鍵寫法
@Entity
@Table(name = "user_segments")
@IDClass(UserSegment.class) //指定複合主鍵類別
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSegment {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "segment_id")
    private Segment segment;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="deleted_at")
    private LocalDateTime deletedAt;
}
*/