package com.cookshare.notification.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationAggregator {

	private final long ONE_DAY_MS = 60000;
	// private final long ONE_DAY_MS = 86400000;
	//private final long FIVE_MINUTES_MS = 300000;

	private final Map<String, Long> lastNotificationTimes = new ConcurrentHashMap<>();

	public boolean isNotificationAllowed(String chatRoomId) {
		long lastTime = lastNotificationTimes.getOrDefault(chatRoomId, 0L);
		long currentTime = System.currentTimeMillis();
		long timeDifference = currentTime - lastTime;

		log.info("마지막 알림 시간 = {}, 현재 시간 = {}, 차이 = {}", lastTime, currentTime, timeDifference);
		return timeDifference > ONE_DAY_MS;
	}

	public void updateNotificationTime(String chatRoomId) {
		long currentTime = System.currentTimeMillis();
		lastNotificationTimes.put(chatRoomId, currentTime); // 알림 시간 업데이트
		log.info("알림 시간 업데이트: 채팅방 ID = {}, 현재 시간 = {}", chatRoomId, currentTime);
	}

}