package com.cookshare.domain;


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
@Document
public class ChatRoom {
	@Id
	private String id; // MongoDB의 고유 식별자
	private String foodId;
	private String firstUser;
	private String secondUser;
	private String urlIdentifier;
	public boolean isMember(String userId) {
		return userId.equals(firstUser) || userId.equals(secondUser);
	}

}