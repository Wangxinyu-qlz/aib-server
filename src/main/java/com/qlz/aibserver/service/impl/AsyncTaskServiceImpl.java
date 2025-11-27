package com.qlz.aibserver.service.impl;

import com.qlz.aibserver.service.AsyncTaskService;
import com.qlz.aibserver.service.async.AsyncTest;
import com.qlz.aibserver.sse.SseEmitterRegistry;
import com.qlz.aibserver.sse.SseEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 异步任务服务实现类
 *
 * @author qlz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncTaskServiceImpl implements AsyncTaskService {

	private final AsyncTest asyncTest;
	private final SseEmitterRegistry sseEmitterRegistry;

	@Override
	public void process() {
		log.info("同步处理");
		asyncTest.processAsync();
	}

	@Override
	@Async("taskExecutor")
	public void submitDemoTask(Long userId, String tabId, String payload) {
		String taskId = UUID.randomUUID().toString();
		log.info("接收异步任务 userId={}, tabId={}, taskId={}", userId, tabId, taskId);

		notify(userId, tabId, SseEventMessage.builder()
				.eventId(taskId + "-accepted")
				.eventType("task-accepted")
				.status("RUNNING")
				.taskId(taskId)
				.tabId(tabId)
				.data(Map.of("payload", payload, "message", "任务已进入执行队列"))
				.timestamp(Instant.now())
				.build());

		try {
			for (int step = 1; step <= 3; step++) {
				Thread.sleep(ThreadLocalRandom.current().nextLong(800, 1500));
				notify(userId, tabId, SseEventMessage.builder()
						.eventId(taskId + "-progress-" + step)
						.eventType("task-progress")
						.status("RUNNING")
						.taskId(taskId)
						.tabId(tabId)
						.data(Map.of(
								"step", step,
								"totalSteps", 3,
								"message", "第" + step + "阶段完成"
						))
						.timestamp(Instant.now())
						.build());
			}

			notify(userId, tabId, SseEventMessage.builder()
					.eventId(taskId + "-done")
					.eventType("task-completed")
					.status("SUCCESS")
					.taskId(taskId)
					.tabId(tabId)
					.data(Map.of(
							"message", "任务处理完成",
							"finishedAt", Instant.now().toString()
					))
					.timestamp(Instant.now())
					.build());
		} catch (InterruptedException interruptedException) {
			Thread.currentThread().interrupt();
			log.warn("异步任务被中断 taskId={}", taskId);
			notifyFailure(userId, tabId, taskId, "任务被中断");
		} catch (Exception exception) {
			log.error("异步任务执行失败 taskId={}", taskId, exception);
			notifyFailure(userId, tabId, taskId, exception.getMessage());
		}
	}

	private void notify(Long userId, String tabId, SseEventMessage message) {
		sseEmitterRegistry.sendToTab(userId, tabId, message);
	}

	private void notifyFailure(Long userId, String tabId, String taskId, String reason) {
		notify(userId, tabId, SseEventMessage.builder()
				.eventId(taskId + "-failed")
				.eventType("task-failed")
				.status("FAILED")
				.taskId(taskId)
				.tabId(tabId)
				.data(Map.of("message", "任务失败", "reason", reason))
				.timestamp(Instant.now())
				.build());
	}
}
