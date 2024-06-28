package com.cookshare.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.cookshare.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
	private Long foodId;
	private List<MultipartFile> images;
	private List<String> imageUrls;
	private String category;
	private LocalDate makeByDate;
	private LocalDate eatByDate;
	private LocalDateTime createdAt;
	private String status;
	private String title;
	private String description;
	private String location;
	// 좌표
	private double latitude;
	private double longitude;
	private User giver;
	private User receiver;
	private Integer likes;
	private Boolean isFavorite; // 로그인한 유저가 이 음식을 찜했는지 여부
}