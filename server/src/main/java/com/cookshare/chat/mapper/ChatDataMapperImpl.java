package com.cookshare.chat.mapper;

import org.springframework.stereotype.Component;

import com.cookshare.chat.dto.ChatMessageDto;
import com.cookshare.chat.dto.ChatRoomCreationDto;
import com.cookshare.chat.dto.ChatRoomDto;
import com.cookshare.domain.ChatMessage;
import com.cookshare.domain.ChatRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatDataMapperImpl implements ChatDataMapper {
	@Override
	public ChatMessageDto toChatMessageDto(ChatMessage message) {
		try {
			return ChatMessageDto.builder()
				.chatRoomId(message.getChatRoomId())
				.sender(message.getSender())
				.content(message.getContent())
				.timestamp(message.getTimestamp())
				.build();
		} catch (Exception e) {
			log.error("ChatMessage DTO 변환 중 오류 발생", e);
			throw e;
		}
	}

	@Override
	public ChatRoomDto toChatRoomDto(ChatRoom room, ChatMessage lastMessage) {
		try {
			return ChatRoomDto.builder()
				.chatRoomId(room.getUrlIdentifier())
				.lastMessage(lastMessage.getContent())
				.lastMessageTimestamp(lastMessage.getTimestamp())
				// .profileImage(room.getProfileImage()) //
				.build();
		} catch (Exception e) {
			log.error("ChatRoom DTO 변환 중 오류 발생", e);
			throw e;
		}
	}

	@Override
	public ChatRoomCreationDto toChatRoomCreationDto(ChatRoom room) {
		try {
			// ChatRoomCreationDto 변환 로직 구현 아직 안함
			return ChatRoomCreationDto.builder()

				.foodId(room.getFoodId())
				.urlIdentifier(room.getUrlIdentifier())
				.build();
		} catch (Exception e) {

			log.error("ChatRoomCreation DTO 변환 중 오류 발생", e);
			throw e;
		}
	}
}