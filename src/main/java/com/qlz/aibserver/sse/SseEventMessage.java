package com.qlz.aibserver.sse;

import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;

/**
 * SSE事件负载
 */
@Builder
public record SseEventMessage(
        String eventId,
        String eventType,
        String status,
        String taskId,
        String tabId,
        Object data,
        Instant timestamp
) implements Serializable {

    public SseEventMessage withDefaults() {
        return SseEventMessage.builder()
                .eventId(eventId)
                .eventType(eventType != null ? eventType : "message")
                .status(status)
                .taskId(taskId)
                .tabId(tabId)
                .data(data)
                .timestamp(timestamp != null ? timestamp : Instant.now())
                .build();
    }
}

