package com.cookshare.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "mytowns")
public class MyTown {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mytown_id", nullable = false, updatable = false)
	private Long myTownId; // 공유 식별자

	@Column(name = "title", nullable = false, length = 255)
	private String title; // 제목

	@Column(name = "description", length = 500)
	private String description; // 설명

	@Column(name = "location", nullable = false, length = 255)
	private String location; // 위치

	@Column(name = "latitude")
	private double latitude;

	@Column(name = "longitude")
	private double longitude;

	@Column(name = "price")
	private Double price; // 가격

	@Column(name = "created_at")
	private Timestamp createdAt; // 생성 날짜

	@Column(name = "expires_at")
	private Timestamp expiresAt; // 만료 날짜

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "status")
	private String status;

	@Column(name = "max_to")
	private Integer maxTO; // 최대 참여 가능 인원수

	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "host_id", referencedColumnName = "user_id")
	private User host;
}
