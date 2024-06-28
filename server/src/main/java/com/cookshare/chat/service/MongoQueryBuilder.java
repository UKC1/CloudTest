package com.cookshare.chat.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cookshare.domain.ChatMessage;
import com.cookshare.domain.UserChatRoomVisibility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoQueryBuilder {
	private final MongoTemplate mongoTemplate;

	public void updateChatRoomVisibility(String userId, String chatRoomId) {
		log.info("updateChatRoomVisibility:  userId={}, chatRoomId ID={}", userId, chatRoomId);

		Query query = new Query(Criteria.where("userId").is(userId).and("chatRoomId").is(chatRoomId));
		Update update = new Update().set("isHidden", false);

		mongoTemplate.updateFirst(query, update, UserChatRoomVisibility.class);

	}

	public long countUnreadMessages(String chatRoomId, String userId) {
		log.debug("countUnreadMessages: chatRoomId={}, userId={}", chatRoomId, userId);

		Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
			.and("sender").ne(userId)
			.and("isRead").is(false));
		log.debug("읽지않은 쿼리: {}", query);
		return mongoTemplate.count(query, ChatMessage.class);
	}

	public void updateMessagesAsRead(String chatRoomId, String userId) {
		Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
			.and("sender").ne(userId)
			.and("isRead").is(false));

		Update update = new Update().set("isRead", true);
		log.info("update ={}", update);
		mongoTemplate.updateMulti(query, update, ChatMessage.class);
	}

	// public long countTotalUnreadMessages(String userId) {
	// 	Query query = new Query(Criteria.where("sender").ne(userId)
	// 		.and("isRead").is(false));
	// 	return mongoTemplate.count(query, ChatMessage.class);
	// }

	public long countTotalUnreadMessages(String userId) {
		Query query = new Query(
			Criteria.where("receiver").is(userId)
				.and("isRead").is(false)
		);
		return mongoTemplate.count(query, ChatMessage.class);
	}

}
