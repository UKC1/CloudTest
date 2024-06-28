package com.cookshare.notification.sse.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.cookshare.chat.service.MongoQueryBuilder;
import com.cookshare.domain.Notification;
import com.cookshare.notification.dto.NotificationDto;
import com.cookshare.notification.repository.NotificationRepository;
import com.cookshare.notification.service.NotificationAggregator;
import com.cookshare.notification.service.NotificationService;
import com.cookshare.notification.sse.component.SseEmitters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseEmitterService {
	private final SseEmitters sseEmitters;
	private final NotificationAggregator notificationAggregator;
	private final NotificationService notificationService;
	private final MongoQueryBuilder mongoQueryBuilder;
	private final NotificationRepository notificationRepository;

	public void processNotification(String receiverId, String senderId, Object data) {
		if (notificationAggregator.isNotificationAllowed(receiverId)) {
			createAndSendNotification(receiverId, senderId, data.toString());

			notificationAggregator.updateNotificationTime(receiverId);
		} else {
			log.info("알림이 허락되지 않았음");
		}
	}

	private void createAndSendNotification(String receiverId, String senderId, String data) {
		Notification notification = notificationService.createNotificationForMessage(
			receiverId, senderId, data
		);
		sendIndividualNotification(receiverId, notification);
	}

	public void sendIndividualNotification(String receiverId, Notification notification) {
		SseEmitter emitter = sseEmitters.getEmitter(receiverId);
		LocalDateTime createdAt = notification.getCreatedAt().toLocalDateTime();

		log.info("receiverId = {}", receiverId);
		if (emitter != null) {
			NotificationDto notificationDto = new NotificationDto(
				notification.getNotificationId(),
				notification.getMessage(),
				notification.getIsRead(),
				notification.getIsSent(),
				createdAt
			);

			try {
				emitter.send(SseEmitter.event()
					.name("notification")
					.data(notificationDto));
				log.info("호출됨");
			} catch (Exception e) {
				sseEmitters.remove(receiverId, emitter);
				log.error("알림보내는거실패  {}", receiverId, e);
			}
		}
	}

	public void sendAllRelevantUpdates(String userId) {
		SseEmitter emitter = sseEmitters.getEmitter(userId);
		if (emitter != null) {
			try {
				// 읽지 않은 채팅 메시지 수
				long unreadChatCount = mongoQueryBuilder.countTotalUnreadMessages(userId);
				// 읽지 않은 알림 수
				long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000; // 일주일을 밀리초로 환산
				Timestamp oneWeekAgo = new Timestamp(System.currentTimeMillis() - oneWeekInMillis);
				long unreadNoticeCount = notificationRepository.countUnreadNotificationsForLastWeek(userId, oneWeekAgo);

				// 하나의 이벤트로 통합 정보 전송
				emitter.send(SseEmitter.event()
					.name("updateUserStatus")
					.data(Map.of(
						"unreadChatCount", unreadChatCount,
						"unreadNoticeCount", unreadNoticeCount
					)));

			} catch (Exception e) {
				emitter.completeWithError(e);
				sseEmitters.remove(userId, emitter);
				log.error(" sendAllRelevantUpdates 실패  userId = {}", userId, e);
			}
		}
	}

}  

