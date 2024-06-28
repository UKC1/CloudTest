package com.cookshare.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookshare.domain.MyTown;

@Repository
public interface MyTownRepository extends JpaRepository<MyTown, Long> {

}
