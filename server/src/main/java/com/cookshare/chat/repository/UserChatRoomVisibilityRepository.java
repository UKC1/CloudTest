package com.cookshare.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import com.cookshare.domain.UserChatRoomVisibility;

@Repository
public interface UserChatRoomVisibilityRepository extends MongoRepository<UserChatRoomVisibility, String> {
	List<UserChatRoomVisibility> findByUserId(String userId);

	Optional<UserChatRoomVisibility> findByUserIdAndChatRoomId(String userId, String chatRoomId);


}