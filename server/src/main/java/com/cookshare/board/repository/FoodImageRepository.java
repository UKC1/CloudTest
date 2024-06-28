package com.cookshare.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookshare.domain.FoodImage;

@Repository
public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {
	// FoodImageRepository
	List<FoodImage> findByFoodFoodId(Long foodId);
}
