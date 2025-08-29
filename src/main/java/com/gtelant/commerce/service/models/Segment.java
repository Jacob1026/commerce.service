package com.gtelant.commerce.service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "segments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name ="name")
    private String name;

    @Column (name ="description")
    private String description;

    @Column (name ="created_at")
    private LocalDateTime createdAt;

    @Column (name ="deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "segment")
    private List<UserSegment> userSegments;
}
