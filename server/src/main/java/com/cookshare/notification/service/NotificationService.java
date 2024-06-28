package com.cookshare.notification.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookshare.domain.Notification;
import com.cookshare.domain.User;
import com.cookshare.notification.repository.NotificationRepository;
import com.cookshare.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public Notification createNotificationForMessage(String receiverMobileNumber, String senderMobileNumber,
		String messageContent) {

		User receiver = findUserByMobileNumber(receiverMobileNumber);
		User sender = findUserByMobileNumber(senderMobileNumber);

		String individualMessage = createIndividualMessage(sender, messageContent);

		return createNotification(receiver, individualMessage);
	}

	private String createIndividualMessage(User sender, String messageContent) {
		return String.format("%s 사용자가 채팅을 보냈습니다:", sender.getMobileNumber());
	}

	public Notification createRoomCreationNotification(String receiverMobileNumber, String senderMobileNumber) {
		User receiver = findUserByMobileNumber(receiverMobileNumber);
		User sender = findUserByMobileNumber(senderMobileNumber);

		String notificationMessage = String.format("%s 사용자와의 채팅방이 생성되었습니다", sender.getNickName());

		return createNotification(receiver, notificationMessage);
	}

	private Notification createNotification(User receiver, String message) {
		Notification notification = Notification.builder()
			.user(receiver)
			.message(message)
			.isRead(false)
			.isSent(true)
			.createdAt(new Timestamp(System.currentTimeMillis()))
			.build();

		return notificationRepository.save(notification);
	}

	private User findUserByMobileNumber(String mobileNumber) {
		return userRepository.findByMobileNumber(mobileNumber).orElseThrow(
			() -> new RuntimeException("사용자를 찾을 수 없습니다.")
		);
	}

	public List<Notification> getUnreadNotifications(Long userId) {
		return notificationRepository.findByUser_UserIdAndIsReadFalse(userId);
	}

	@Transactional
	public void updateNotificationAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new IllegalStateException("Notification not found"));
		notification.setIsRead(true);
		notificationRepository.save(notification);
	}

}
