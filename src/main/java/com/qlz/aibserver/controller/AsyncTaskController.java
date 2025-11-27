package com.qlz.aibserver.controller;

import com.qlz.aibserver.base.BaseController;
import com.qlz.aibserver.base.Result;
import com.qlz.aibserver.dto.req.AsyncTaskTriggerReq;
import com.qlz.aibserver.service.AsyncTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 异步任务控制器
 * 提供异步任务处理的REST API接口
 * 
 * @author qlz
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/async/tasks")
@RequiredArgsConstructor
public class AsyncTaskController extends BaseController {

    private final AsyncTaskService asyncTaskService;

	@PostMapping("/demo")
	public Result<String> triggerDemoTask(@RequestBody @Validated AsyncTaskTriggerReq request) {
		log.info("触发演示异步任务 userId={}, tabId={}", request.getUserId(), request.getTabId());
		asyncTaskService.submitDemoTask(request.getUserId(), request.getTabId(), request.getPayload());
		return convertSuccessResult("任务已提交");
	}

	/**
	 * 兼容旧接口
	 */
	@Deprecated
	@RequestMapping("/test")
	public Result<String> test() {
		log.info("进入controller");
		asyncTaskService.process();
		return convertSuccessResult("ok");
	}
}
