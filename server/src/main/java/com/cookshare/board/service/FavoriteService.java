package com.cookshare.board.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookshare.board.exception.DuplicateFavoriteException;
import com.cookshare.board.exception.NotFoundException;
import com.cookshare.board.repository.FavoriteRepository;
import com.cookshare.board.repository.FoodRepository;
import com.cookshare.domain.FavoriteFood;
import com.cookshare.domain.Food;
import com.cookshare.domain.User;
import com.cookshare.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final FoodRepository foodRepository;

	public boolean addFavorite(Long foodId, Long userId) throws DuplicateFavoriteException {
		boolean isExistingFavorite = favoriteRepository.existsByFoodFoodIdAndUserUserId(foodId, userId);
		if (isExistingFavorite) {
			Optional<FavoriteFood> favoriteFood = favoriteRepository.findByFoodFoodIdAndUserUserId(foodId, userId);
			favoriteFood.ifPresent(favoriteRepository::delete);
			throw new DuplicateFavoriteException("Duplicate favorite entry for foodId: " + foodId + " and userId: " + userId);
		}
		Optional<User> user = userRepository.findById(userId);
		Optional<Food> food = foodRepository.findById(foodId);
		if (user.isPresent() && food.isPresent()) {
			FavoriteFood favoriteFood = new FavoriteFood();
			favoriteFood.setUser(user.get());
			favoriteFood.setFood(food.get());
			favoriteRepository.save(favoriteFood);
			return true;
		}
		throw new NotFoundException("User or Food not found.");
	}

	public boolean removeFavorite(Long foodId, Long userId) {
		Optional<FavoriteFood> favoriteFood = favoriteRepository.findByFoodFoodIdAndUserUserId(foodId, userId);
		if (favoriteFood.isPresent()) {
			favoriteRepository.delete(favoriteFood.get());
			return true;
		}
		throw new NotFoundException("Favorite not found.");
	}
}

