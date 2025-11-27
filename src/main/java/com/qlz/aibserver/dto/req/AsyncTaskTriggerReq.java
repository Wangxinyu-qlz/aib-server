package com.qlz.aibserver.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 触发异步任务请求体
 */
@Data
public class AsyncTaskTriggerReq {

    /**
     * 当前操作用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 浏览器标签页唯一标识
     */
    @NotBlank(message = "tabId不能为空")
    private String tabId;

    /**
     * 任务附加信息
     */
    private String payload;
}

