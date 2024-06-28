package com.cookshare.notification.sse.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeartbeatService {
	private final ScheduledExecutorService scheduler;

	public void startHeartbeat(SseEmitter emitter, long interval, TimeUnit timeUnit) {
		scheduler.scheduleAtFixedRate(() -> {
			try {
				emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		}, 0, interval, timeUnit);
	}
}