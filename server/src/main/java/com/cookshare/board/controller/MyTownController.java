package com.cookshare.board.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookshare.board.dto.MyTownDTO;
import com.cookshare.board.service.MyTownService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/myTowns")
@RequiredArgsConstructor
public class MyTownController {
	private final MyTownService myTownService;

	@GetMapping
	public ResponseEntity<Page<MyTownDTO>> getAllCommunities(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<MyTownDTO> MyTownDTOS = myTownService.getAllMyTowns(pageable);
		return ResponseEntity.ok(MyTownDTOS);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MyTownDTO> getMyTownById(Long id) {
		MyTownDTO MyTownDTO = myTownService.getMyTownById(id);
		return ResponseEntity.ok(MyTownDTO);
	}

	@PostMapping
	public ResponseEntity<MyTownDTO> createMyTown(MyTownDTO MyTownDTO) {
		MyTownDTO createdMyTown = myTownService.createMyTown(MyTownDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMyTown);
	}

}
