package com.qlz.aibserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qlz.aibserver.dto.req.LoginReq;
import com.qlz.aibserver.dto.resp.LoginResp;
import com.qlz.aibserver.dto.req.RegisterReq;
import com.qlz.aibserver.entity.User;
import com.qlz.aibserver.exception.BusinessException;
import com.qlz.aibserver.mapper.UserMapper;
import com.qlz.aibserver.service.UserService;
import com.qlz.aibserver.util.JwtUtil;
import com.qlz.aibserver.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 * 
 * @author qlz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterReq registerReq) {
        // 验证密码强度
        if (!PasswordUtil.isValidPassword(registerReq.getPassword())) {
            throw new BusinessException(PasswordUtil.getPasswordStrengthMessage(registerReq.getPassword()));
        }

        // 验证确认密码
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        if (existsByUsername(registerReq.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (registerReq.getEmail() != null && !registerReq.getEmail().trim().isEmpty()) {
            if (existsByEmail(registerReq.getEmail())) {
                throw new BusinessException("邮箱已被注册");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerReq.getUsername());
        user.setPassword(PasswordUtil.encode(registerReq.getPassword()));
        user.setEmail(registerReq.getEmail());
        user.setNickname(registerReq.getNickname() != null ? registerReq.getNickname() : registerReq.getUsername());
        user.setStatus(1); // 默认启用状态

        // 保存用户
        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new RuntimeException("用户注册失败");
        }

        log.info("用户注册成功: {}", registerReq.getUsername());
        return "注册成功";
    }

    @Override
    public LoginResp login(LoginReq loginReq) {
        // 根据用户名查询用户
        User user = findByUsername(loginReq.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new IllegalArgumentException("账户已被禁用");
        }

        // 验证密码
        if (!PasswordUtil.matches(loginReq.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 生成JWT令牌
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        // 构建用户信息
        LoginResp.UserInfo userInfo = LoginResp.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .build();

        // 构建登录响应
        LoginResp response = LoginResp.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getExpirationDateFromToken(accessToken).getTime() / 1000)
                .userInfo(userInfo)
                .build();

        log.info("用户登录成功: {}", loginReq.getUsername());
        return response;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMapper.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }
}
