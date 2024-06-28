package com.cookshare.notification.notificationutils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		// 원하는 크기로 스레드 풀을 설정할 수 있습니다.
		return Executors.newScheduledThreadPool(1);
	}
}