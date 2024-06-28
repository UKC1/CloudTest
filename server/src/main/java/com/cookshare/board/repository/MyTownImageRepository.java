package com.cookshare.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookshare.domain.MyTownImage;

@Repository
public interface MyTownImageRepository extends JpaRepository<MyTownImage, Long> {
	List<MyTownImage> findByMyTownMyTownId(Long myTownId);
}
