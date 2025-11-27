package com.qlz.aibserver.base;

import com.qlz.aibserver.enums.ReturnCodeEnum;

/**
 * 统一响应结果
 * 
 * @author qlz
 */
public class Result<T> {
    private static final long serialVersionUID = 7163242669595049214L;

    private T data;

    private Integer code;

    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result() {
    }

    public Result(ReturnCodeEnum returnCodeEnum) {
        this.data = null;
        this.code = returnCodeEnum.getCode();
        this.message = returnCodeEnum.getDesc();
    }
    public Result(ReturnCodeEnum returnCodeEnum, String message) {
        this.data = null;
        this.code = returnCodeEnum.getCode();
        this.message = message;
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应带数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ReturnCodeEnum.SUCCESS.getCode());
        result.setMessage(ReturnCodeEnum.SUCCESS.getDesc());
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode(ReturnCodeEnum.BUSINESS_ERROR.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应带错误码
     */
    public static <T> Result<T> fail(ReturnCodeEnum returnCodeEnum) {
        Result<T> result = new Result<>();
        result.setCode(returnCodeEnum.getCode());
        result.setMessage(returnCodeEnum.getDesc());
        return result;
    }

    /**
     * 失败响应带错误码和自定义消息
     */
    public static <T> Result<T> fail(ReturnCodeEnum returnCodeEnum, String message) {
        Result<T> result = new Result<>();
        result.setCode(returnCodeEnum.getCode());
        result.setMessage(message);
        return result;
    }
}
