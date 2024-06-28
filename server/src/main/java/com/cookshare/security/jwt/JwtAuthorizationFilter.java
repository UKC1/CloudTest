package com.cookshare.security.jwt;

import com.cookshare.domain.User;
import com.cookshare.security.dto.CustomUserDetails;
import com.cookshare.security.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookshare.security.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.cookshare.security.jwt.SecurityConstants.REFRESH_EXPIRATION_TIME;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${com.cookshare.jwt.secret-key}")
    private String secretKey;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Autowired
    public JwtAuthorizationFilter(JwtUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info("JWT 필터링을 시작합니다: " + path);

        // 인증이 필요없는 경로 목록
        List<String> noAuthRequiredPaths = Arrays.asList(
            "/api/user/login",
            "/api/user/register",
            "/api/user/memberPhoneCheck",
            "/api/user/checkNickName"
        );

        // 현재 요청 경로가 인증이 필요없는 경로 목록에 포함되어 있는지 확인
        boolean isAuthNotRequired = noAuthRequiredPaths.stream().anyMatch(path::startsWith);

        // 인증이 필요없는 경로에 대한 요청이면 필터 체인을 계속 진행하고 메소드를 종료
        if (isAuthNotRequired) {
            filterChain.doFilter(request, response);
            return;
        }

        if (response.isCommitted()) {
            log.info("응답이 이미 작성되었습니다. 필터 체인을 중단합니다.");
            return;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            if (isTokenBlacklisted(token)) {
                unauthorizedResponse(response, "블랙리스트된 토큰입니다.");
                return; // 응답 후 메소드 종료
            }

            if (!jwtUtil.isExpired(token)) {
                processToken(token, request, response, filterChain);
                return;
            } else {
                handleExpiredToken(request, response, filterChain);
                return; // 토큰 만료 처리 후 메소드 종료
            }
        }

        // 다른 인증 조건이 없는 경우 기본 처리
        filterChain.doFilter(request, response);
    }


    private boolean isTokenBlacklisted(String token) {
        String key = "BLACKLISTED_TOKEN:" + token;
        return redisService.isTokenBlacklisted(key);
    }

    private boolean processToken(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (!jwtUtil.isExpired(token)) {
                log.info("토큰 유효함: " + token);
                processAuthentication(token, response);
                filterChain.doFilter(request, response);
                return true;
            } else {
                handleExpiredToken(request, response, filterChain);
            }
        } catch (ExpiredJwtException e) {
            log.warn("토큰 만료: " + token);
            handleExpiredToken(request, response, filterChain);
        }
        return false;
    }


    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "Refresh-Token".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (refreshTokenCookie != null && !jwtUtil.isRefreshTokenExpired(refreshTokenCookie.getValue())) {
            renewTokens(refreshTokenCookie.getValue(), response);
            if (!response.isCommitted()) {
                filterChain.doFilter(request, response);
            }
        } else {
            unauthorizedResponse(response, refreshTokenCookie == null ? "리프레쉬 토큰이 없습니다." : "리프레쉬 토큰이 만료 되었습니다.");
        }

    }

    private void renewTokens(String refreshToken, HttpServletResponse response) throws IOException {

        if (refreshToken.startsWith("Bearer")) {
            refreshToken = refreshToken.substring("Bearer".length()).trim();
        }
        log.info("리프레시 토큰 갱신: " + refreshToken);

        // '+'를 '%2B'로 치환하여 디코딩
        refreshToken = refreshToken.replace("%2B", "+");
        String NewrefreshToken2 = URLDecoder.decode(refreshToken, StandardCharsets.UTF_8.toString());

        Long userId = jwtUtil.getUserId(NewrefreshToken2);
        String username = jwtUtil.getMobileNumber(NewrefreshToken2);
        String role = jwtUtil.getRole(NewrefreshToken2);
        String location = jwtUtil.getLocation(NewrefreshToken2);
        String nickName = jwtUtil.getNickName(NewrefreshToken2);

        String newAccessToken = jwtUtil.createAccessToken(userId,username, role,location, nickName);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, username, role,location, nickName);
        long accessTokenExpirationMs = System.currentTimeMillis() + EXPIRATION_TIME;
        long refreshTokenExpirationMs = System.currentTimeMillis() + REFRESH_EXPIRATION_TIME;

        // 쿠키에 새 리프레시 토큰 설정 (인코딩)
        String encodedNewRefreshToken = URLEncoder.encode("Bearer " + newRefreshToken, StandardCharsets.UTF_8.toString());
        Cookie refreshCookie = new Cookie("Refresh-Token", encodedNewRefreshToken);

        // 테스트용 :false ,  원래는 : true
        refreshCookie.setHttpOnly(false);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        // Redis에 다시 저장
        redisService.saveToken(username, newRefreshToken, REFRESH_EXPIRATION_TIME);

        // 헤더에 새 토큰으로 변경
        response.addHeader( "Authorization", "Bearer " + newAccessToken);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // ISO 8601 형식

        Map<String, Object> responseData  = new HashMap<>();
        responseData .put("새로운 엑세스토큰(accessToken)", newAccessToken);
        responseData .put("새로운 리프레시토큰(refreshToken)", newRefreshToken);
        responseData .put("새로운 엑세스토큰유효기간(accessTokenExpiresIn)", dateFormat.format(new Date(accessTokenExpirationMs)));
        responseData .put("새로운 리프레시토큰유효기간(refreshTokenExpiresIn)", dateFormat.format(new Date(refreshTokenExpirationMs)));
        responseData .put("role", role);

        if (!response.isCommitted()) {
            response.setContentType("application/json; charset=utf8");
            response.getWriter().write("{\"message\": \"토큰이 유효하고 사용자가 인증되었습니다.\"}");
            response.getWriter().flush();
        }

        return;
    }

    private void processAuthentication(String token, HttpServletResponse response) throws IOException {
        log.info("사용자 인증 처리: " + token);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);
        User user = new User();
        user.setUserId(userId);
        user.setMobileNumber(username);
        user.setRole(role);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // if (!response.isCommitted()) {
        //     response.setContentType("application/json; charset=utf8");
        //     response.getWriter().write("{\"message\": \"토큰이 유효하고 사용자가 인증되었습니다.\"}");
        //     response.getWriter().flush();
        // }
    }

    private void unauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=utf8");
            response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
            response.getWriter().flush();
        }
    }
}
