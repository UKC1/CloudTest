package com.cookshare.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cookshare.domain.Notification;
import com.cookshare.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping("/unread")
	public List<Notification> getUnreadNotifications(@RequestParam Long userId) {
		return notificationService.getUnreadNotifications(userId);
	}

	@PutMapping("/updateAsRead/{notificationId}")
	public void markNotificationAsRead(@PathVariable Long notificationId) {
		notificationService.updateNotificationAsRead(notificationId);
	}

}
