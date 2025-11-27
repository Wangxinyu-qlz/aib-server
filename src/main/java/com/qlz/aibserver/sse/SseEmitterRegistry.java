package com.qlz.aibserver.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE连接注册中心
 */
@Slf4j
@Component
public class SseEmitterRegistry {

    private static final long DEFAULT_TIMEOUT = Duration.ofMinutes(15).toMillis();

    private final Map<String, SseEmitter> emitterStore = new ConcurrentHashMap<>();
    private final Map<Long, Set<String>> userEmitterIndex = new ConcurrentHashMap<>();

    public SseEmitter register(Long userId, String tabId) {
        Assert.notNull(userId, "userId can not be null");
        Assert.isTrue(StringUtils.hasText(tabId), "tabId can not be blank");

        String key = buildKey(userId, tabId);
        remove(userId, tabId);

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitter.onCompletion(() -> remove(userId, tabId));
        emitter.onTimeout(() -> {
            log.debug("SSE连接超时 userId={}, tabId={}", userId, tabId);
            remove(userId, tabId);
        });
        emitter.onError(error -> {
            log.warn("SSE连接异常 userId={}, tabId={}, error={}", userId, tabId, error.getMessage());
            remove(userId, tabId);
        });

        emitterStore.put(key, emitter);
        userEmitterIndex.computeIfAbsent(userId, id -> ConcurrentHashMap.newKeySet()).add(key);
        sendInternal(emitter, SseEventMessage.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("connected")
                .status("OK")
                .tabId(tabId)
                .data(Map.of("message", "SSE连接已建立", "connectedAt", Instant.now().toString()))
                .timestamp(Instant.now())
                .build());
        return emitter;
    }

    public void sendToTab(Long userId, String tabId, SseEventMessage message) {
        if (message == null) {
            return;
        }
        String key = buildKey(userId, tabId);
        Optional.ofNullable(emitterStore.get(key))
                .ifPresentOrElse(
                        emitter -> sendInternal(emitter, message),
                        () -> log.info("SSE连接不存在，userId={}, tabId={}", userId, tabId)
                );
    }

    public void sendToUser(Long userId, SseEventMessage message) {
        if (message == null) {
            return;
        }
        Set<String> keys = userEmitterIndex.get(userId);
        if (keys == null || keys.isEmpty()) {
            log.info("用户{}暂无有效SSE连接", userId);
            return;
        }
        keys.stream()
                .map(emitterStore::get)
                .filter(emitter -> emitter != null)
                .forEach(emitter -> sendInternal(emitter, message));
    }

    public void remove(Long userId, String tabId) {
        String key = buildKey(userId, tabId);
        SseEmitter emitter = emitterStore.remove(key);
        if (emitter != null) {
            emitter.complete();
        }
        userEmitterIndex.computeIfPresent(userId, (uid, keys) -> {
            keys.remove(key);
            return keys.isEmpty() ? null : keys;
        });
    }

    private String buildKey(Long userId, String tabId) {
        return userId + ":" + tabId;
    }

    private void sendInternal(SseEmitter emitter, SseEventMessage rawMessage) {
        SseEventMessage message = rawMessage.withDefaults();
        try {
            emitter.send(SseEmitter.event()
                    .id(Optional.ofNullable(message.eventId()).orElse(UUID.randomUUID().toString()))
                    .name(message.eventType())
                    .data(message));
        } catch (IOException | IllegalStateException ex) {
            log.warn("SSE发送失败，将关闭连接: {}", ex.getMessage());
            emitter.completeWithError(ex);
        }
    }
}

