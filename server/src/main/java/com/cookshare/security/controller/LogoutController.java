package com.cookshare.security.controller;

import com.cookshare.security.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
public class LogoutController {

    @Value("${com.cookshare.jwt.secret-key}")
    private String secretKeyEncoded;

    @Autowired
    private RedisService redisService;

    @DeleteMapping("/api/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String mobileNumber = extractMobileNumberFromToken(token);
                if (mobileNumber != null) {
                    redisService.deleteToken(mobileNumber);
                    removeCookie("Refresh-Token", request, response);

                    long duration = getRemainingTime(token);
                    redisService.blacklistToken(token, duration);

                    return "로그아웃 성공: 모바일 번호로 인증되었습니다. (쿠키 제거 및 Redis 블랙리스트 처리)";
                }
            } catch (SignatureException e) {
                System.err.println("JWT 서명 검증 실패: " + e.getMessage());
                return "유효하지 않은 토큰으로 로그아웃 실패";
            }
        }
        return "유효하지 않은 로그아웃 요청입니다.";
    }

    private void removeCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    private String extractMobileNumberFromToken(String token) throws SignatureException {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKeyEncoded.getBytes(StandardCharsets.UTF_8))
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get("mobileNumber", String.class);
    }

    public long getRemainingTime(String token) throws SignatureException {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKeyEncoded.getBytes(StandardCharsets.UTF_8))
            .build()
            .parseClaimsJws(token)
            .getBody();
        Date expiration = claims.getExpiration();
        long duration = expiration.getTime() - System.currentTimeMillis();
        return Math.max(duration / 1000, 1);
    }
}
