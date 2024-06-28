package com.cookshare.notification.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
	private Long notificationId;
	private String message;
	private Boolean isRead;
	private Boolean isSent;
	private LocalDateTime createdAt;
}
