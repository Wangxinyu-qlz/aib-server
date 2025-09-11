package com.qlz.aibserver.service.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @program: aib-server
 * @author: Qiaolezi
 * @create: 2025-09-23 21:32
 * @description:
 **/
@Service
@Async("taskExecutor")
@Slf4j
public class AsyncTest {
	public void processAsync() {
		log.info("开始异步处理");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		log.info("异步处理完成");

	}
}
