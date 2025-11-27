package com.qlz.aibserver.util;

import cn.hutool.crypto.digest.BCrypt;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * 密码工具类
 * 
 * @author qlz
 * @since 2024-01-01
 */
@Slf4j
public class PasswordUtil {

    /**
     * 密码强度正则表达式：至少包含一个英文字母和一个数字
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$");

    /**
     * 验证密码强度
     * 密码要求：
     * 1. 长度大于6位
     * 2. 至少包含英文字母和数字组合
     * 
     * @param password 密码
     * @return 是否符合要求
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // 检查长度
        if (password.length() < 6) {
            return false;
        }
        
        // 检查是否包含英文字母和数字
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * 加密密码
     * 
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        try {
            return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        } catch (Exception e) {
            log.error("密码加密失败", e);
            throw new RuntimeException("密码加密失败");
        }
    }

    /**
     * 验证密码
     * 
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        try {
            return BCrypt.checkpw(rawPassword, encodedPassword);
        } catch (Exception e) {
            log.error("密码验证失败", e);
            return false;
        }
    }

    /**
     * 获取密码强度提示信息
     * 
     * @param password 密码
     * @return 提示信息
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "密码不能为空";
        }
        
        if (password.length() < 6) {
            return "密码长度必须大于6位";
        }
        
        if (!password.matches(".*[A-Za-z].*")) {
            return "密码必须包含至少一个英文字母";
        }
        
        if (!password.matches(".*\\d.*")) {
            return "密码必须包含至少一个数字";
        }
        
        return "密码强度符合要求";
    }
}
