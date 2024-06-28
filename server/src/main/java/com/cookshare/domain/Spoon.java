package com.cookshare.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "spoon")
public class Spoon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spoon_id")
    private Long spoonId;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "giver_id", referencedColumnName = "user_id")
    private User giver;

    @Column(name = "score")
    private Integer score;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // Getters and Setters
}
