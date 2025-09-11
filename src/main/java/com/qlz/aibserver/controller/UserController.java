package com.qlz.aibserver.controller;

import com.qlz.aibserver.base.BaseController;
import com.qlz.aibserver.base.Result;
import com.qlz.aibserver.dto.req.RegisterReq;
import com.qlz.aibserver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 
 * @author qlz
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class UserController extends BaseController {

    private final UserService userService;

    /**
     * 用户注册
     * 
     * @param registerReq 注册请求
     * @return 注册结果
     */
	@PostMapping("register")
	public Result<String> register(@Valid @RequestBody RegisterReq registerReq) {
		return convertSuccessResult(userService.register(registerReq));
	}


    /**
     * 用户登录
     * 
     * @param loginRequest 登录请求
     * @return 登录结果
     */


    /**
     * 检查用户名是否可用
     * 
     * @param username 用户名
     * @return 检查结果
     */


    /**
     * 检查邮箱是否可用
     * 
     * @param email 邮箱
     * @return 检查结果
     */

}
