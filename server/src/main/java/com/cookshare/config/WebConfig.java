package com.cookshare.config;// package com.foodshare.config;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//
// 	@Value("${file.location-dir}")
// 	private String imgDir;
// 	@Override
// 	public void addResourceHandlers(ResourceHandlerRegistry registry) {
// 		registry.addResourceHandler("/download/**")
// 			.addResourceLocations(imgDir);
// 	}
//
// 	@Override
// 	public void addCorsMappings(CorsRegistry registry) {
// 		registry.addMapping("/**")
// 			.allowedOrigins("http://localhost:3000") // 클라이언트 서버 주소
// 			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
// 			.allowCredentials(true);
// 	}
//
// }