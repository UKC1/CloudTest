package com.cookshare.security.service;

import com.cookshare.domain.User;
import com.cookshare.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    @Override
    public User registerUser(User user) {
        // 사용자의 비밀번호를 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll(); // 모든 사용자 조회
    }



    @Override
    public User updateUserByMobileNumber(User updatedUser) {
        // 사용자의 존재 여부를 확인
        Optional<User> userOptional = userRepository.findByMobileNumber(updatedUser.getMobileNumber());
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            // 필요한 필드만 업데이트
            existingUser.setNickName(updatedUser.getNickName());
            existingUser.setLocation(updatedUser.getLocation());
            existingUser.setMobileNumber(updatedUser.getMobileNumber()); // 모바일 번호도 업데이트 가능하다고 가정

            // 업데이트된 사용자 정보 저장
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("번호로 등록된 유저가 없습니다: " + updatedUser.getMobileNumber());
        }
    }


    @Override
    public void deleteUserByMobileNumber(String mobileNumber) {
        Optional<User> userOptional = userRepository.findByMobileNumber(mobileNumber);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new RuntimeException("번호로 등록된 유저가 없습니다: " + mobileNumber);
        }
    }


}