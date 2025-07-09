package com.cinema.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cinema.entity.User;

import java.math.BigDecimal;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param username 用户名
     * @return 分页结果
     */
    Page<User> pageUsers(Page<User> page, String username);

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    User getUserById(Integer id);

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean register(User user);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String username, String password);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean removeUser(Integer id);

    /**
     * 更新用户余额
     *
     * @param userId 用户ID
     * @param amount 金额
     * @return 是否成功
     */
    boolean updateBalance(Integer userId, BigDecimal amount);
    
    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Integer userId, String status);
    
    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updatePassword(Integer userId, String oldPassword, String newPassword);
}
