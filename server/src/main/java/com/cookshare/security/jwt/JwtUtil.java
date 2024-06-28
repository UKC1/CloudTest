package com.cookshare.security.jwt;


import com.cookshare.security.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private SecretKey secretKey;  // secretKey는 다른 곳에서 관리 및 초기화되고 있다고 가정

    public JwtUtil(@Value("${com.cookshare.jwt.secret-key}") String secretKeyString) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }


    @Autowired
    private RedisService redisService;

    public Long getUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.get("userId", String.class));
        } catch (Exception e) {
            System.out.println("유저ID 파싱 에러 : " + e.getMessage());
            return null;  // 또는 적절한 예외 처리
        }
    }


    public String getMobileNumber(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("mobileNumber", String.class);
    }

    public String getLocation(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("location", String.class);
    }

    public String getNickName(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("nickName", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }


    public boolean isRefreshTokenExpired(String refreshToken) {
        try {
            System.out.println("디코딩 진입");

            if (refreshToken.startsWith("Bearer")) {
                refreshToken = refreshToken.substring("Bearer".length()).trim();
            }

            System.out.println("Bearer 접두사 제거 후: " + refreshToken);

            // '+'를 '%2B'로 치환하여 디코딩
            refreshToken = refreshToken.replace("%2B", "+");
            String NewrefreshToken = URLDecoder.decode(refreshToken, StandardCharsets.UTF_8.toString());
            System.out.println("디코딩 후: " + NewrefreshToken);

            // 토큰에서 정보 추출
            Long userId = getUserId(NewrefreshToken);
            String mobileNumber = getMobileNumber(NewrefreshToken);
            String role = getRole(NewrefreshToken);
            String location = getLocation(NewrefreshToken);
            String nickName = getNickName(NewrefreshToken);
            Date expiration = getTokenExpiration(NewrefreshToken);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // ISO 8601 형식

            // 추출된 정보 출력
            System.out.println("User ID: " + userId);
            System.out.println("Mobile Number: " + mobileNumber);
            System.out.println("Role: " + role);
            System.out.println("Location: " + location);
            System.out.println("Nick Name: " + nickName);
            System.out.println("Refresh Token Expires In: " + dateFormat.format(expiration));


            // 토큰 만료 여부 직접 확인
            if (isExpired(NewrefreshToken)) {
                System.out.println("토큰 만료됨");
                return true;  // 토큰이 만료되었음을 표시
            } else {
                // Redis를 통해 추가 토큰 유효성 검사 수행
                if (this.isTokenValid(NewrefreshToken)) {
                    System.out.println("토큰 유효함(Redis에서 확인함)");
                    return false; // 토큰이 만료되지 않았고 유효함을 표시
                } else {
                    System.out.println("토큰 무효(Redis에서 확인불가)");
                    return true; // 토큰이 유효하지 않음을 표시
                }
            }

        } catch (JwtException | IllegalArgumentException | UnsupportedEncodingException e) {
            // 토큰 파싱 실패 또는 만료된 경우
            System.out.println("예외 발생: " + e.getMessage());
            return true;
        }
    }

    public boolean isTokenValid(String NewrefreshToken) {
        String mobileNumber = getMobileNumber(NewrefreshToken); // 토큰에서 모바일 번호 추출
        System.out.println(mobileNumber);

        // Redis에서 해당 모바일 번호로 저장된 refreshToken 가져오기
        String storedRefreshToken = redisService.getRefreshToken(mobileNumber);
        System.out.println(storedRefreshToken);
        System.out.println(NewrefreshToken);
        if (storedRefreshToken != null && storedRefreshToken.equals((NewrefreshToken).trim())) {
            System.out.println("Redis에 저장된 refreshToken과 일치함. 토큰 유효함.");
            return true;
        } else {
            System.out.println("Redis에 저장된 refreshToken과 불일치하거나 존재하지 않음. 토큰 무효임.");
            return false;
        }
    }


    // 엑세스 토큰 생성
    public String createAccessToken(Long userId, String mobileNumber, String role, String location, String nickName) {
        return Jwts.builder()
                .claim("userId", String.valueOf(userId))
                .claim("mobileNumber", mobileNumber)
                .claim("role", "ROLE_" + role.toUpperCase())
                .claim( "location", location)
                .claim("nickName", nickName )
                .setIssuedAt(new Date(System.currentTimeMillis())) // 현재 발행시간
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))   // 소멸시간
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

    }
    // 리프레시 토큰 생성
    public String createRefreshToken(Long userId, String mobileNumber, String role, String location, String nickName) {
        return Jwts.builder()
                .claim("userId", String.valueOf(userId))
                .claim("mobileNumber", mobileNumber)
                .claim("role", "ROLE_" + role.toUpperCase())
                .claim( "location", location)
                .claim("nickName", nickName )
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +  SecurityConstants.REFRESH_EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

    }

    public Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new RuntimeException("토큰이 유효하지 않습니다", e);
        }
    }

    public Date getTokenExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 이름(아이디) 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    public String getRemainingTime(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            long duration = expiration.getTime() - System.currentTimeMillis(); // 만료 시간과 현재 시간의 차이

            return formatDuration(duration);
        } catch (JwtException e) {
            System.err.println("JWT processing error: " + e.getMessage());
            return "Invalid token";
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return "Error processing token";
        }
    }

    private String formatDuration(long durationMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60;

        return String.format("%d시 %d분 %d초", hours, minutes, seconds);
    }



}