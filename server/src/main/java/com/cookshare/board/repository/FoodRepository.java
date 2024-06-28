package com.cookshare.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookshare.domain.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
	// 기본적으로 Food 정보만 조회하고, 연관된 이미지나 카테고리는 서비스에서 처리
	List<Food> findAll();

	// FoodRepository.java
	boolean existsByCategoryCategoryId(Long categoryId);

	List<Food> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
	List<Food> findByCategoryNameContainingIgnoreCase(String categoryName);


}