package com.cookshare.domain;

import java.util.Date;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chatMessages")
public class ChatMessage {
	@Id
	private String id;
	@Indexed
	private String chatRoomId;
	private String sender;
	private String receiver;
	private String content;

	@Indexed(direction = IndexDirection.DESCENDING)
	private Date timestamp;

	@Indexed
	private boolean isRead = false;
}