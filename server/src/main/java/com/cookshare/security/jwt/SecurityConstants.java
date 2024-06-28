package com.cookshare.security.jwt;

// Security 및 JWT 관련된 상수를 관리하는 클래스 ( 엑세스 , 리프레쉬 토큰 만료시간 설정 )

public class SecurityConstants {
    // 토큰 타입
    public  static final String TOKEN_TYPE = "JWT";
    // 헤더의 접두사
    public static final String TOKEN_PREFIX = "Bearer";
    // JWT 토큰을 담을 HTTP 요청 헤더 이름
    public static final String TOKEN_HEADER = "Authorization";
    // 엑세스 토큰 시간
    public static final long EXPIRATION_TIME = 30L * 60 * 10000; // 1 분
    // 리프레시 토큰 담길 헤더 이름 (테스트용 , redis 저장)
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    // 리프레시 토큰 만료 시간
    public static final long REFRESH_EXPIRATION_TIME = 50L * 60 * 10000; // 5분


}
