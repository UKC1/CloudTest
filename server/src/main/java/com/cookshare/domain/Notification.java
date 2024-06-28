package com.cookshare.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "notifications")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long notificationId;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@Column(name = "type")
	private Integer type;

	@Column(name = "message")
	private String message;

	@Column(name = "is_read")
	private Boolean isRead;

	@Column(name = "is_sent")
	private Boolean isSent = false;

	@Column(name = "created_at")
	private Timestamp createdAt;

}