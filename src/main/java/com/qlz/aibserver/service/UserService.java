package com.qlz.aibserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qlz.aibserver.dto.req.LoginReq;
import com.qlz.aibserver.dto.resp.LoginResp;
import com.qlz.aibserver.dto.req.RegisterReq;
import com.qlz.aibserver.entity.User;

/**
 * 用户服务接口
 * 
 * @author qlz
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * 
     * @param registerReq 注册请求
     * @return 注册结果
     */
    String register(RegisterReq registerReq);

    /**
     * 用户登录
     * 
     * @param loginReq 登录请求
     * @return 登录响应
     */
    LoginResp login(LoginReq loginReq);

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
}
