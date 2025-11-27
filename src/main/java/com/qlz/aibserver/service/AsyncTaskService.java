package com.qlz.aibserver.service;

/**
 * 异步任务服务接口
 * 
 * @author qlz
 */
public interface AsyncTaskService {
	void process();

	/**
	 * 提交一个演示异步任务，完成后通过SSE推送给指定tab
	 *
	 * @param userId 用户ID
	 * @param tabId  标签页ID
	 * @param payload 任务附加信息
	 */
	void submitDemoTask(Long userId, String tabId, String payload);
}
