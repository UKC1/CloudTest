package com.cookshare.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cookshare.board.dto.FoodDTO;
import com.cookshare.board.dto.MyTownDTO;
import com.cookshare.board.exception.NotFoundException;
import com.cookshare.board.mapper.MyTownMapper;
import com.cookshare.board.repository.CategoryRepository;
import com.cookshare.board.repository.MyTownImageRepository;
import com.cookshare.board.repository.MyTownRepository;
import com.cookshare.domain.Category;
import com.cookshare.domain.FavoriteFood;
import com.cookshare.domain.FoodImage;
import com.cookshare.domain.MyTown;
import com.cookshare.domain.MyTownImage;
import com.cookshare.security.dto.CustomUserDetails;
import com.cookshare.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyTownService {
	private final MyTownRepository myTownRepository;
	private final MyTownImageRepository myTownImageRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final MyTownMapper myTownMapper;

	private Long getUserIdFromAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
		} else {
			throw new UsernameNotFoundException("Authentication failed - no valid user found.");
		}
	}
	public Page<MyTownDTO> getAllMyTowns(Pageable pageable) {
		return myTownRepository.findAll(pageable).map(myTown -> {
			List<MyTownImage> myTownImages = myTownImageRepository.findByMyTownMyTownId(myTown.getMyTownId());
			Category category = categoryRepository.findById(myTown.getCategory().getCategoryId()).orElseThrow(() -> new NotFoundException("Category not found"));
			MyTownDTO myTownDTO = myTownMapper.convertToMyTownDTO(myTown, myTownImages, category);
			return myTownDTO;
		});
	}

	public MyTownDTO createMyTown(MyTownDTO communityDTO) {
		return null;
	}

	public MyTownDTO getMyTownById(Long id) {
		MyTown myTown = myTownRepository.findById(id).orElseThrow(() -> new NotFoundException("MyTown not found"));
		List<MyTownImage> myTownImages = myTownImageRepository.findByMyTownMyTownId(myTown.getMyTownId());
		Category category = categoryRepository.findById(myTown.getCategory().getCategoryId()).orElseThrow(() -> new NotFoundException("Category not found"));
		return myTownMapper.convertToMyTownDTO(myTown, myTownImages, category);
	}
}
