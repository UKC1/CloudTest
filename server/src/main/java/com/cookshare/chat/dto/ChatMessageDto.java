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
public class ChatMessageDto {
	private String chatRoomId;
	private String sender;
	private String content;
	private Date timestamp;

}