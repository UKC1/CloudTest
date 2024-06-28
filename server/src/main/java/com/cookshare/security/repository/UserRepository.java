package com.cookshare.security.repository;

import com.cookshare.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileNumber(String mobileNumber);
    Optional<User> findByNickName(String nickName);

	Optional<User> findByUserId(Long userId);
}
