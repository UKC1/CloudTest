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
public class ChatRoomCreationDto {
	private String chatRoomId;
	private String foodId;
	private String firstUserMobileNumber;
	private String secondUserMobileNumber;
	private String urlIdentifier;
	private Date createdAt;
}