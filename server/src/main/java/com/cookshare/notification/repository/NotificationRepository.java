package com.cookshare.notification.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cookshare.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("SELECT COUNT(n) FROM Notification n WHERE n.user.mobileNumber = :userId AND n.isRead = false AND n.createdAt >= :oneWeekAgo")
	long countUnreadNotificationsForLastWeek(@Param("userId") String userId, @Param("oneWeekAgo") Timestamp oneWeekAgo);

	List<Notification> findByUser_UserIdAndIsReadFalse(Long userId);
}
