package com.qlz.aibserver.controller;

import com.qlz.aibserver.base.BaseController;
import com.qlz.aibserver.base.Result;
import com.qlz.aibserver.service.AsyncTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 异步任务控制器
 * 提供异步任务处理的REST API接口
 * 
 * @author qlz
 */
@Slf4j
@RestController
@RequestMapping("/async")
@RequiredArgsConstructor
public class AsyncTaskController extends BaseController {

    private final AsyncTaskService asyncTaskService;

	@RequestMapping("/test")
	public Result<String> test() {
		log.info("进入controller");

		asyncTaskService.process();

		return convertSuccessResult("ok");
	}
}
