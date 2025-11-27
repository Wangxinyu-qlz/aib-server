package com.qlz.aibserver.controller;

import com.qlz.aibserver.base.BaseController;
import com.qlz.aibserver.base.Result;
import com.qlz.aibserver.sse.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE订阅控制器
 */
@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SseSubscriptionController extends BaseController {

    private final SseEmitterRegistry sseEmitterRegistry;

    @GetMapping(value = "/sub/{userId}/{tabId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userId, @PathVariable String tabId) {
        log.info("建立SSE订阅 userId={}, tabId={}", userId, tabId);
        return sseEmitterRegistry.register(userId, tabId);
    }

    @GetMapping("/sub/ping")
    public Result<String> ping() {
        return convertSuccessResult("pong");
    }
}

