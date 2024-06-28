package com.cookshare.config;// package com.foodshare.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
//
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
//
// 	@Bean
// 	public PasswordEncoder passwordEncoder() {
// 		return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 인스턴스를 반환
// 	}
//
// 	// Spring Security의 HTTP 요청에 대한 보안 설정을 추가합니다.
//
// 	@Bean
// 	public WebSecurityCustomizer webSecurityCustomizer() {
// 		return (web) -> web.ignoring().requestMatchers(
// 			"/api/users",
// 			"/api/users/signup",
// 			// "/api/users/login",
// 			"/Register");
// 	}
//
// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		http
// 			//폼 기반 로그인 비 활성화
// 			.formLogin((login) -> login.disable())
// 			//Http 기본 인증 비활성화
// 			.httpBasic((basic) -> basic.disable())
// 			//.httpBasic(Customizer.withDefaults());
// 			.csrf((csrf) -> csrf.disable());
//
// 		//                .securityMatchers((matchers) -> matchers
// 		//                        .requestMatchers("/api/**")
// 		//                )
// 		//                .authorizeHttpRequests((authorize) -> authorize
// 		//                        .requestMatchers("/api/users/login").permitAll() // 로그인 경로 허용
// 		//                        .anyRequest().hasRole("USER")
// 		//                );
//
// 		return http.build();
// 	}
// }
//
//
//
