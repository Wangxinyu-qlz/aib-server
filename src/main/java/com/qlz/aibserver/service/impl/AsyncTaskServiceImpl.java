package com.qlz.aibserver.service.impl;

import com.qlz.aibserver.service.AsyncTaskService;
import com.qlz.aibserver.service.async.AsyncTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 异步任务服务实现类
 * 
 * @author qlz
 */
@Slf4j
@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {
	@Autowired
	private AsyncTest asyncTest;

	@Override
	public void process() {
		log.info("同步处理");
		asyncTest.processAsync();
	}
}
