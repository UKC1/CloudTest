package com.cookshare.chat.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cookshare.chat.dto.ChatMessageDto;
import com.cookshare.chat.dto.ChatRequstDto;
import com.cookshare.chat.dto.ChatRoomCreateRequest;
import com.cookshare.chat.dto.ChatRoomCreationDto;
import com.cookshare.chat.dto.ChatRoomDto;
import com.cookshare.chat.service.ChatRoomMessageService;

import com.cookshare.chat.service.ChatRoomService;

import com.cookshare.chat.service.MongoQueryBuilder;
import com.cookshare.chat.service.VisibilityService;

import com.cookshare.chat.utils.ValidationUtils;
import com.cookshare.domain.ChatRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/chat")
@Slf4j

@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	private final ChatRoomMessageService chatRoomMessageService;
	private final VisibilityService visibilityService;
	private final MongoQueryBuilder mongoQueryBuilder;

	@GetMapping("/detailRoom/{chatRoomId}/messages")
	public ResponseEntity<Slice<ChatMessageDto>> listChatRoomMessages(
		@PathVariable("chatRoomId") String chatRoomUrlId,
		@RequestParam String userId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		try {
			log.info("페이징 메시지 상세 조회 시작");
			Optional<ChatRoom> chatRoomOpt = chatRoomService.findByUrlIdentifier(chatRoomUrlId);

			if (chatRoomOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Slice<ChatMessageDto> messages = chatRoomMessageService.listMessagesInChatRoom(
				chatRoomOpt.get().getUrlIdentifier(), userId, page, size);

			if (messages.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(messages);

		} catch (Exception e) {
			log.error("메시지 상세 조회 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/detailRoom/updateAsRead")
	public ResponseEntity<Void> updateMessagesAsRead(@RequestBody ChatRequstDto chatRequestDto) {
		try {
			String chatRoomId = ValidationUtils.validateNotEmpty(chatRequestDto.getChatRoomId(), "chatRoomId");
			String userId = ValidationUtils.validateNotEmpty(chatRequestDto.getUserId(), "userId");
			mongoQueryBuilder.updateMessagesAsRead(chatRoomId, userId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("메시지 읽음 처리 중 오류 발생", e);  // TODO: 4/26/24 나중에는 로그말고 다른 예외처리
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/ListRooms")
	public ResponseEntity<List<ChatRoomDto>> listChatRooms(@RequestParam String userId) {
		try {
			log.info("채팅 목록 조회 시작");
			List<ChatRoomDto> chatRoomDtos = chatRoomService.listChatRoomsForUser(userId);
			return ResponseEntity.ok(chatRoomDtos);
		} catch (Exception e) {
			log.error("채팅 목록 조회 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 예외 처리
		}
	}

	@PutMapping("/hideRoom/{chatRoomId}")
	public ResponseEntity<Void> hideChatRoom(@RequestBody ChatRequstDto hideRequestData) {
		try {
			String chatRoomId = ValidationUtils.validateNotEmpty(hideRequestData.getChatRoomId(), "chatRoomId");
			String userId = ValidationUtils.validateNotEmpty(hideRequestData.getUserId(), "userId");

			visibilityService.setRoomHidden(userId, chatRoomId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("채팅방 숨김 처리 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 채팅방 생성
	@PostMapping("/createRoom")
	public ResponseEntity<ChatRoomCreationDto> createRoom(@RequestBody ChatRoomCreateRequest request) {
		try {
			String firstUserId = request.getFirstUserMobileNumber();
			String secondUserId = request.getSecondUserMobileNumber();
			String foodId = request.getFoodId();
			log.debug("요청 내용: {}", request);

			ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(firstUserId, secondUserId, foodId);

			ChatRoomCreationDto chatRoomDto = chatRoomService.toChatRoomCreationDto(chatRoom);

			log.debug("chatRoomDto: {}", chatRoomDto);
			return ResponseEntity.ok(chatRoomDto);
		} catch (Exception e) {
			log.error("채팅방 생성 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}






