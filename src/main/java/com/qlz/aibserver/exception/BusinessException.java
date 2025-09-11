package com.qlz.aibserver.exception;

import com.qlz.aibserver.enums.ReturnCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况
 * 
 * @author qlz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final ReturnCodeEnum returnCodeEnum;

	public BusinessException(String message) {
		super(message);
		this.returnCodeEnum = null;
	}

	public BusinessException(ReturnCodeEnum returnCodeEnum) {
		super(returnCodeEnum.getDesc());
		this.returnCodeEnum = returnCodeEnum;
	}

	public BusinessException(String message, ReturnCodeEnum returnCodeEnum) {
		super(message);
		this.returnCodeEnum = returnCodeEnum;
	}
}
