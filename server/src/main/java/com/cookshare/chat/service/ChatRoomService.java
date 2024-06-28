package com.cookshare.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cookshare.chat.annotation.LogExecutionTime;
import com.cookshare.chat.dto.ChatRoomCreationDto;
import com.cookshare.chat.dto.ChatRoomDto;
import com.cookshare.chat.mapper.ChatDataMapper;
import com.cookshare.chat.repository.ChatMessageRepository;
import com.cookshare.chat.repository.ChatRoomRepository;
import com.cookshare.domain.ChatMessage;
import com.cookshare.domain.ChatRoom;
import com.cookshare.chat.utils.ValidationUtils;
import com.cookshare.domain.Notification;
import com.cookshare.domain.User;
import com.cookshare.notification.service.NotificationService;
import com.cookshare.notification.sse.service.SseEmitterService;
import com.cookshare.security.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final VisibilityService visibilityService;
	private final ChatDataMapper chatDataMapper;
	private final ChatMessageRepository chatMessageRepository;
	private final UserServiceImpl userService;
	private final MongoQueryBuilder mongoQueryBuilder;

	private final NotificationService notificationService;
	private final SseEmitterService sseEmitterService;

	@LogExecutionTime
	public List<ChatRoomDto> listChatRoomsForUser(String userId) {
		log.debug("listChatRoomsForUser: userId={}", userId);
		Set<String> hiddenRoomIds = visibilityService.getHiddenRoomIds(userId);
		List<ChatRoomDto> chatRooms = listAvailableChatRooms(userId, hiddenRoomIds);
		updateUnreadCounts(chatRooms, userId);
		return chatRooms;
	}

	public ChatRoom findOrCreateChatRoom(String firstUserId, String secondUserId, String foodId) {
		log.info("채팅방 서비스 레이어에 들어왔습니다");

		User firstUser = userService.findByMobileNumber(firstUserId)
			.orElseThrow(() -> new IllegalStateException(firstUserId + " ID를 가진 사용자를 찾을 수 없습니다."));
		User secondUser = userService.findByMobileNumber(secondUserId)
			.orElseThrow(() -> new IllegalStateException(secondUserId + " ID를 가진 사용자를 찾을 수 없습니다."));

		String chatRoomId = firstUserId + "_" + secondUserId;
		String urlIdentifier = firstUser.getNickName() + "_" + secondUser.getNickName();

		return findByUrlIdentifier(urlIdentifier)
			.orElseGet(() -> createAndNotifyNewChatRoom(firstUserId, secondUserId, foodId, chatRoomId, urlIdentifier));
	}

	private List<ChatRoomDto> listAvailableChatRooms(String userId, Set<String> hiddenRoomIds) {
		log.debug(" listAvailableChatRooms");
		return chatRoomRepository.findByIdContaining(userId).stream()
			.filter(room -> !hiddenRoomIds.contains(room.getUrlIdentifier()))
			.map(this::convertRoomToDto)
			.collect(Collectors.toList());
	}

	private ChatRoom createAndNotifyNewChatRoom(String firstUserId, String secondUserId, String foodId,
		String chatRoomId, String urlIdentifier) {
		ChatRoom newRoom = createChatRoom(firstUserId, secondUserId, foodId, chatRoomId, urlIdentifier);
		createNotifyNewChatRoom(firstUserId, newRoom);
		return newRoom;
	}

	private void createNotifyNewChatRoom(String userId, ChatRoom chatRoom) {
		Notification notification = notificationService.createRoomCreationNotification(userId,
			chatRoom.getSecondUser());
		sseEmitterService.sendIndividualNotification(userId, notification);
	}

	private ChatRoomDto convertRoomToDto(ChatRoom room) {
		log.debug("convertRoomToDto: roomId={}", room.getUrlIdentifier());

		ChatMessage lastMessage = chatMessageRepository.findFirstByChatRoomIdOrderByTimestampDesc(
				room.getUrlIdentifier())
			.orElse(ChatMessage.builder()
				.chatRoomId(room.getUrlIdentifier())
				.sender("")
				.content("빈 메시지")
				.timestamp(new Date())
				.isRead(false)
				.build());
		log.debug("마지막 메시지: {}", lastMessage);

		return chatDataMapper.toChatRoomDto(room, lastMessage);
	}

	private void updateUnreadCounts(List<ChatRoomDto> chatRooms, String userId) {
		chatRooms.forEach(room -> {
			long unreadCount = mongoQueryBuilder.countUnreadMessages(room.getChatRoomId(), userId);
			room.setUnreadCount(unreadCount);
			log.debug("채팅방 ID={}, 읽지 않은 메시지 수={}", room.getChatRoomId(), unreadCount);
		});
	}

	public ChatRoom createChatRoom(String firstUserId, String secondUserId, String foodId, String chatRoomId,
		String urlIdentifier) {
		log.debug("createChatRoom");
		ValidationUtils.validateNotEmpty(firstUserId, "firstUserId");
		ValidationUtils.validateNotEmpty(secondUserId, "secondUserId");

		ChatRoom chatRoom = ChatRoom.builder()
			.id(chatRoomId)
			.firstUser(firstUserId)
			.secondUser(secondUserId)
			.foodId(foodId)
			.urlIdentifier(urlIdentifier)
			.build();
		log.debug("chatRoomId={}", chatRoom);
		return chatRoomRepository.save(chatRoom);
	}

	public ChatRoomCreationDto toChatRoomCreationDto(ChatRoom room) {
		return chatDataMapper.toChatRoomCreationDto(room);
	}

	public Optional<ChatRoom> findByUrlIdentifier(String chatRoomUrlId) {
		return chatRoomRepository.findByUrlIdentifier(chatRoomUrlId);
	}

}
