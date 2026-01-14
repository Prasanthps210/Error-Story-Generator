package com.example.errorstory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String story;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
