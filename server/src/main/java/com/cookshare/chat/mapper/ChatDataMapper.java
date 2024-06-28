package com.cookshare.chat.mapper;

import com.cookshare.chat.dto.ChatMessageDto;
import com.cookshare.chat.dto.ChatRoomCreationDto;
import com.cookshare.chat.dto.ChatRoomDto;
import com.cookshare.domain.ChatMessage;
import com.cookshare.domain.ChatRoom;

public interface ChatDataMapper {
	ChatMessageDto toChatMessageDto(ChatMessage message);

	ChatRoomDto toChatRoomDto(ChatRoom room, ChatMessage lastMessage);

	ChatRoomCreationDto toChatRoomCreationDto(ChatRoom room);
}
 