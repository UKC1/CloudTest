package com.cookshare.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyTownDTO {
	private Long myTownId;
	private String title;
	private String description;
	private String location;
	private double latitude;
	private double longitude;
	private double price;
	private String category;
	private LocalDateTime createdAt;
	private LocalDateTime expiresAt;
	private String status;
	private List<String> imageUrls;
	private List<MultipartFile> images;
	private Integer maxTO;

}
