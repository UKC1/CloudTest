package com.cookshare.domain;

import java.util.Date;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "userChatRoomVisibility")
@TypeAlias("userChatRoomVisibility")
public class UserChatRoomVisibility {
	@Id
	private String id;

	private String userId;
	private String chatRoomId;
	private String lastVisibleMessageId;

	@Field("isHidden")
	private boolean isHidden;

	private Date lastHiddenTimestamp;
}