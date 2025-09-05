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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //外來鍵，來自segment
    @ManyToOne
    @JoinColumn(name = "segment_id")
    private Segment segment;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="deleted_at")
    private LocalDateTime deletedAt;
}