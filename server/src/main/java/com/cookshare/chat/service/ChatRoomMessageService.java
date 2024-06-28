package com.cookshare.chat.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cookshare.chat.annotation.LogExecutionTime;
import com.cookshare.chat.dto.ChatMessageDto;
import com.cookshare.chat.mapper.ChatDataMapper;
import com.cookshare.chat.repository.ChatMessageRepository;
import com.cookshare.domain.UserChatRoomVisibility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomMessageService {
	private final ChatMessageRepository chatMessageRepository;
	private final ChatDataMapper chatDataMapper;
	private final VisibilityService visibilityService;
	private final MongoQueryBuilder mongoQueryBuilder;

	@LogExecutionTime
	public Slice<ChatMessageDto> listMessagesInChatRoom(String chatRoomId, String userId, int page, int size) {
		PageRequest pageable = createPageRequest(page, size);

		log.info("listMessagesInChatRoom: chatRoomId={}, userId={}, page={}, size={}", chatRoomId, userId, page, size);

		Slice<ChatMessageDto> messages = processMessagesBasedOnVisibility(userId, chatRoomId, pageable);
		log.info("Messages returned: {}", messages.getContent());

		mongoQueryBuilder.updateMessagesAsRead(chatRoomId, userId);

		return messages;
	}

	private Slice<ChatMessageDto> processMessagesBasedOnVisibility(String userId, String chatRoomId,
		PageRequest pageable) {
		Optional<UserChatRoomVisibility> visibilityOpt = visibilityService.getUserChatRoomVisibility(userId,
			chatRoomId);
		log.info("processMessagesBasedOnVisibility: userId={}, chatRoomId={}, visibilityOpt={}", userId, chatRoomId,
			visibilityOpt);

		return visibilityOpt.map(visibility -> filterMessagesByVisibility(visibility, chatRoomId, pageable))
			.orElseGet(() -> getChatRoomMessages(chatRoomId, pageable));
	}

	private Slice<ChatMessageDto> filterMessagesByVisibility(UserChatRoomVisibility visibility, String chatRoomId,
		PageRequest pageable) {
		return Optional.ofNullable(visibility.getLastHiddenTimestamp())
			.map(lastHidden -> getMessagesSince(chatRoomId, lastHidden, pageable))
			.orElseGet(() -> getChatRoomMessages(chatRoomId, pageable));
	}

	public Slice<ChatMessageDto> getChatRoomMessages(String chatRoomId, PageRequest pageable) {
		log.info("쿼리 요청: chatRoomId={}, page={}, size={}", chatRoomId, pageable.getPageNumber(),
			pageable.getPageSize());

		Slice<ChatMessageDto> messages = chatMessageRepository.findByChatRoomIdOrderByTimestampDesc(chatRoomId,
				pageable)
			.map(chatDataMapper::toChatMessageDto);

		if (messages.isEmpty()) {
			log.warn("쿼리 결과: 메시지 없음");
		} else {
			log.info("쿼리 결과: 메시지 수={}, 첫 번째 메시지 내용={}", messages.getNumberOfElements(),
				messages.getContent().isEmpty() ? "없음" : messages.getContent().get(0).getContent());
		}

		return messages;
	}

	private Slice<ChatMessageDto> getMessagesSince(String chatRoomId, Date startTimestamp, PageRequest pageable) {
		return chatMessageRepository.findByChatRoomIdAndTimestampGreaterThan(chatRoomId, startTimestamp, pageable)
			.map(chatDataMapper::toChatMessageDto);
	}

	private PageRequest createPageRequest(int page, int size) {
		return PageRequest.of(page, size, Sort.by("timestamp").descending());
	}

}