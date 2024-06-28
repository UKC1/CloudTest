package com.cookshare.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "location")
    private String location;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(ZonedDateTime.now().toInstant());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.from(ZonedDateTime.now().toInstant());
    }
}
