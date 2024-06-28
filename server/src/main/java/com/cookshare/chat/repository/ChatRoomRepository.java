package com.cookshare.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cookshare.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
	//List<ChatRoom> findByFirstUserOrSecondUser(String firstUserId, String secondUserId);
	@Query("{'_id': {$regex: ?0, $options: 'i'}}")
	List<ChatRoom> findByIdContaining(String userId);

	//Optional<ChatRoom> findByIdentifier(String chatRoomId);
	Optional<ChatRoom> findByUrlIdentifier(String urlIdentifier);

}

