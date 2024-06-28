package com.cookshare.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.cookshare.board.exception.DuplicateFavoriteException;
import com.cookshare.board.service.FavoriteService;
import com.cookshare.security.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

	private final FavoriteService favoriteService;

	@PostMapping("/{foodId}")
	public ResponseEntity<?> addFavorite(@PathVariable Long foodId) {
		log.info("Adding favorite for foodId: {}", foodId);
		try {
			Long userId = getUserIdFromToken(); // 토큰에서 userId 추출
			boolean result = favoriteService.addFavorite(foodId, userId);
			return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
		} catch (DuplicateFavoriteException e) {
			log.error("Failed to add favorite: " + e.getMessage());
			return ResponseEntity.badRequest().body("Failed to add favorite: " + e.getMessage());
		}
	}

	private Long getUserIdFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
		} else {
			throw new UsernameNotFoundException("Authentication failed - no valid user found.");
		}
	}

	@DeleteMapping("/{foodId}")
	public ResponseEntity<?> removeFavorite(@PathVariable Long foodId) {
		log.info("Removing favorite for foodId: {}", foodId);
		boolean result = favoriteService.removeFavorite(foodId, getUserIdFromToken());
		return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
	}
}
