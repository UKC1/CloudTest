package com.cookshare.chat.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
	private String chatRoomId;
	private String lastMessage;
	private Date lastMessageTimestamp;
	private String profileImage;
	private long unreadCount;
}
