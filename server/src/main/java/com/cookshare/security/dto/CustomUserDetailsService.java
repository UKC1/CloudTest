package com.cookshare.security.dto;

import com.cookshare.domain.User;
import com.cookshare.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        User user = userRepository.findByMobileNumber(mobileNumber)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + mobileNumber));

        return new CustomUserDetails(user);
    }
}