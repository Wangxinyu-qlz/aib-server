package com.qlz.aibserver.base;

import com.qlz.aibserver.dto.resp.UserResp;
import com.qlz.aibserver.enums.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: aib-server
 * @author: Qiaolezi
 * @create: 2025-09-20 10:16
 * @description:
 **/
@Slf4j
public class BaseController {

	protected UserResp getCurrentUser() {
		return null;
	}

	protected <T> Result<T> convertSuccessResult(T data) {
		Result<T> result = new Result<>();
		result.setCode(ReturnCodeEnum.SUCCESS.getCode());
		result.setData(data);
		return result;
	}

	protected <T> Result<T> convertFailResult(T data, String message) {
		Result<T> result = new Result<>();
		result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
		result.setData(data);
		result.setMessage(message);
		return result;
	}

	protected <T> Result<T> convertFailResult(T data, String message, ReturnCodeEnum returnCodeEnum) {
		Result<T> result = new Result<>();
		result.setCode(returnCodeEnum.getCode());
		result.setData(data);
		result.setMessage(message);
		return result;
	}
}
