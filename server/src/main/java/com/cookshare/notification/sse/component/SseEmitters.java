package com.cookshare.notification.sse.component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitters {

	private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	public void add(String userId, SseEmitter emitter) {

		emitters.put(userId, emitter);
	}

	public void remove(String userId, SseEmitter emitter) {
		emitters.remove(userId, emitter);
	}

	public SseEmitter getEmitter(String userId) {
		return emitters.get(userId);
	}

	public void removeExistingEmitter(String userId) {
		Optional<SseEmitter> existingEmitterOpt = Optional.ofNullable(emitters.get(userId));
		existingEmitterOpt.ifPresent(existingEmitter -> {
			existingEmitter.complete();
			emitters.remove(userId, existingEmitter);
		});
	}

	public void sendEvent(SseEmitter emitter, String eventName, String data) {
		try {
			emitter.send(SseEmitter.event().name(eventName).data(data));
		} catch (Exception e) {
			throw new RuntimeException("SSE 이벤트 전송 오류", e);
		}
	}
}

