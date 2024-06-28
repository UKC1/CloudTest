package com.cookshare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cookshare.security.jwt.JwtAuthenticationFilter;
import com.cookshare.security.jwt.JwtAuthorizationFilter;
import com.cookshare.security.jwt.JwtUtil;
import com.cookshare.security.service.RedisService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    @Value("${file.location-dir}")
    private String imgDir;

    @Autowired
    public WebSecurityConfig(AuthenticationConfiguration configuration, JwtUtil jwtUtil, RedisService redisService) {
        this.authenticationConfiguration = configuration;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable())
            .csrf(csrf -> csrf.disable())
            .rememberMe(rememberMe -> rememberMe.disable())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/**").permitAll()  // 모든 경로에 대해 접근을 허용
                // .requestMatchers("/api/public/**").permitAll()  // 공개 API
                // .requestMatchers("/api/foods/**").permitAll()  // 공개 API
                // .requestMatchers("/**").permitAll()  // 공개 API
                // .requestMatchers("/download/**").permitAll()  // 정적 리소스 다운로드 경로
                // .requestMatchers("/api/user/**").hasRole("USER")  // 일반 사용자가 접근 가능한 경로
                // .requestMatchers("/api/admin/**").hasRole("ADMIN")  // 관리자가 접근 가능한 경로
                // .requestMatchers("/api/profile/**").authenticated()  // 프로필 관련 API, 로그인된 사용자만 접근 가능
                // .anyRequest().authenticated()  // 그 외 모든 요청은 인증이 필요
            )
            .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/download/**")
            .addResourceLocations(imgDir);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowCredentials(true);
    }
}
