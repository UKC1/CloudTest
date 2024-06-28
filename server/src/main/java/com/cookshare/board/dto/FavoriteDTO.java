package com.cookshare.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {
	private Long userId;
	private Long foodId;
	private Boolean isFavorite; // 찜 추가 또는 삭제를 위한 플래그
}
