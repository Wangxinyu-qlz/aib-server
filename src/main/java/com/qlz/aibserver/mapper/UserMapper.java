package com.qlz.aibserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qlz.aibserver.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问层
 * 
 * @author qlz
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 存在返回true，否则返回false
     */
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 存在返回true，否则返回false
     */
    boolean existsByEmail(@Param("email") String email);
}
