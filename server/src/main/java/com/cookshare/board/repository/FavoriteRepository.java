package com.cookshare.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookshare.domain.FavoriteFood;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteFood, Long> {

	Optional<FavoriteFood> findByFoodFoodIdAndUserUserId(Long foodId, Long userId);

	long countByFoodFoodId(Long foodId);

	Optional<FavoriteFood> findByFoodFoodId(Long foodId);

	boolean existsByFoodFoodIdAndUserUserId(Long foodId, Long userId);

    boolean existsByFoodFoodId(Long foodId);

	void deleteByFoodFoodId(Long foodId);
}
