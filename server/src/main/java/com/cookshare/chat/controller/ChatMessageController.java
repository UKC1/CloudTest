package com.cookshare.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.cookshare.chat.dto.ChatMessageDto;
import com.cookshare.chat.service.MessageProcessingService;
import com.nimbusds.jose.shaded.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

	private final SimpMessagingTemplate template;
	private final MessageProcessingService messageProcessingService;

	@MessageMapping("/chat.room/{chatRoomId}/sendMessage")
	public void message(@DestinationVariable String chatRoomId, @Payload ChatMessageDto message) {
		log.info("채팅 시작");
		log.debug(message.toString());
		log.info("메시지 컨트롤러의 챗 아이디={}", chatRoomId);
		log.info("메시지 컨트롤러의 챗 메시지={}", message);

		messageProcessingService.addMessageToChatRoom(message.getChatRoomId(), message.getSender(),
			message.getContent());

		template.convertAndSend(String.format("/topic/chat/room/%s", chatRoomId), message);

		template.convertAndSend(
			"/topic/chat/room/updates",
			new Gson().toJson(message));
	}

	public void updateChatRoomList() {

		template.convertAndSend("/topic/chat/room/updates", "목록 업데이트"); // 대상 경로 직접 지정
	}

}