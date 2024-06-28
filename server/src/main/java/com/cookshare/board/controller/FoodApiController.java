package com.cookshare.board.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cookshare.board.dto.FoodDTO;
import com.cookshare.board.exception.FileStorageException;
import com.cookshare.board.service.FileStorageService;
import com.cookshare.board.service.FoodService;
import com.cookshare.domain.Food;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/api/foods")
@RestController
@Slf4j
public class FoodApiController {

	private final FoodService foodService;
	private final FileStorageService fileStorageService;

	@GetMapping("/search/by-category")
	public ResponseEntity<List<FoodDTO>> searchFoodsByCategory(@RequestParam(name = "query") String categoryName) {
		log.info("Searching for foods with category name: {}", categoryName);
		List<FoodDTO> searchResults = foodService.searchFoodsByCategoryName(categoryName);
		return ResponseEntity.ok(searchResults);
	}

	@GetMapping("/{id}")
	public ResponseEntity<FoodDTO> read(@PathVariable("id") Long id) {
		log.info("Reading food with id: {}", id);
		FoodDTO foodDTO = foodService.read(id);
		return ResponseEntity.ok(foodDTO);
	}
	@GetMapping
	public ResponseEntity<Page<FoodDTO>> getAllFoods(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		log.info("Retrieving all foods with pageable: {}", pageable);
		Page<FoodDTO> foodDTOs = foodService.getAllFoods(pageable);
		return ResponseEntity.ok(foodDTOs);
	}
	@PostMapping
	public ResponseEntity<?> create(@ModelAttribute(name="formData") FoodDTO foodDTO) {
		log.info("Creating food with data: {}", foodDTO);
		foodDTO.setImageUrls(processUploadedFiles(foodDTO.getImages()));
		Food createdFood = foodService.create(foodDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdFood);
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @ModelAttribute(name="formData") FoodDTO foodDTO) {
		log.info("Updating food with id: {}", id);
		FoodDTO existingFoodDTO = foodService.read(id);

		// 새로운 이미지만 있는 경우
		if (foodDTO.getImageUrls() == null && foodDTO.getImages() != null) {
			foodDTO.setImageUrls(processUploadedFiles(foodDTO.getImages()));
		}
		// 기존 이미지 + 새로운 이미지
		else if (foodDTO.getImageUrls() != null && foodDTO.getImages() != null) {
			for (String imageUrl : processUploadedFiles(foodDTO.getImages())) {
				foodDTO.getImageUrls().add(imageUrl);
			}
		}
		// 기존 이미지에서 수정
		else if (foodDTO.getImages() == null && foodDTO.getImageUrls() != null) {
			foodDTO.setImageUrls(foodDTO.getImageUrls());
		}
		// 둘 다 없으면 그대로
		else if (foodDTO.getImages() == null && foodDTO.getImageUrls() == null) {
			foodDTO.setImageUrls(existingFoodDTO.getImageUrls());
		}

		Food updatedFood = foodService.update(id, foodDTO);
		return ResponseEntity.ok(updatedFood);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		log.info("Deleting food with id: {}", id);
		foodService.delete(id);
		return ResponseEntity.ok().build();
	}
	private List<String> processUploadedFiles(List<MultipartFile> images) {
		List<String> fileDownloadUrls = new ArrayList<>();
		try {
			fileDownloadUrls = fileStorageService.storeFiles(images);
		} catch (FileStorageException e) {
			throw new RuntimeException(e);
		}
		return fileDownloadUrls;
	}
}