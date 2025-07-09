package com.cinema.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cinema.common.exception.BusinessException;
import com.cinema.common.result.ResultCode;
import com.cinema.entity.User;
import com.cinema.mapper.UserMapper;
import com.cinema.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Page<User> pageUsers(Page<User> page, String username) {
        log.debug("分页查询用户列表: page={}, username={}", page, username);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(User::getUsername, username);
        }
        wrapper.orderByDesc(User::getRegisterTime);
        
        Page<User> result = page(page, wrapper);
        log.debug("分页查询用户列表结果: 总记录数={}, 总页数={}", result.getTotal(), result.getPages());
        return result;
    }

    @Override
    public User getUserById(Integer id) {
        log.debug("根据ID获取用户: id={}", id);
        
        User user = getById(id);
        if (user == null) {
            log.warn("用户不存在: id={}", id);
            throw new BusinessException("用户不存在");
        }
        
        log.debug("获取用户成功: id={}, username={}", id, user.getUsername());
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        log.debug("根据用户名获取用户: username={}", username);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = getOne(wrapper);
        
        if (user != null) {
            log.debug("获取用户成功: username={}, userId={}", username, user.getUserId());
        } else {
            log.debug("用户不存在: username={}", username);
        }
        
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(User user) {
        log.info("用户注册: username={}", user.getUsername());
        
        // 验证用户名是否已存在
        User existUser = getUserByUsername(user.getUsername());
        if (existUser != null) {
            log.warn("注册失败，用户名已存在: username={}", user.getUsername());
            throw new BusinessException("用户名已存在");
        }
        
        // 设置默认值
        user.setRegisterTime(LocalDateTime.now());
        if (user.getRole() == null) {
            user.setRole("USER");
        } else {
            // 将角色统一转为大写
            user.setRole(user.getRole().toUpperCase());
        }
        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }
        
        // 密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        log.debug("密码加密完成");
        
        boolean result = save(user);
        if (result) {
            log.info("用户注册成功: userId={}, username={}", user.getUserId(), user.getUsername());
        } else {
            log.error("用户注册失败: username={}", user.getUsername());
        }
        
        return result;
    }

    @Override
    public User login(String username, String password) {
        log.info("用户登录: username={}", username);
        
        // 验证用户名是否存在
        User user = getUserByUsername(username);
        if (user == null) {
            log.warn("登录失败，用户名不存在: username={}", username);
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }
        
        // 验证用户状态
        if (user.getStatus() != null && !user.getStatus().equals("ACTIVE")) {
            log.warn("登录失败，用户状态异常: username={}, status={}", username, user.getStatus());
            if (user.getStatus().equals("BANNED")) {
                throw new BusinessException(ResultCode.UNAUTHORIZED, "账户已被禁用，请联系管理员");
            } else if (user.getStatus().equals("INACTIVE")) {
                throw new BusinessException(ResultCode.UNAUTHORIZED, "账户未激活，请联系管理员");
            } else {
                throw new BusinessException(ResultCode.UNAUTHORIZED, "账户状态异常，请联系管理员");
            }
        }
        
        // 验证密码是否正确
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        log.debug("密码加密完成，准备验证");
        
        if (!user.getPassword().equals(encryptedPassword)) {
            log.warn("登录失败，密码错误: username={}", username);
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }
        
        log.info("用户登录成功: userId={}, username={}", user.getUserId(), user.getUsername());
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        log.info("更新用户信息: userId={}", user.getUserId());
        
        User existUser = getById(user.getUserId());
        if (existUser == null) {
            log.warn("更新失败，用户不存在: userId={}", user.getUserId());
            throw new BusinessException("用户不存在");
        }
        
        // 如果修改了用户名，需要验证新用户名是否已存在
        if (!existUser.getUsername().equals(user.getUsername())) {
            log.debug("用户名已修改，验证新用户名是否可用: oldUsername={}, newUsername={}", 
                    existUser.getUsername(), user.getUsername());
            
            User userByUsername = getUserByUsername(user.getUsername());
            if (userByUsername != null) {
                log.warn("更新失败，新用户名已存在: newUsername={}", user.getUsername());
                throw new BusinessException("用户名已存在");
            }
        }
        
        // 如果修改了密码，需要加密
        if (StringUtils.hasText(user.getPassword()) && !existUser.getPassword().equals(user.getPassword())) {
            log.debug("密码已修改，进行加密");
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        
        boolean result = updateById(user);
        if (result) {
            log.info("更新用户信息成功: userId={}", user.getUserId());
        } else {
            log.error("更新用户信息失败: userId={}", user.getUserId());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUser(Integer id) {
        log.info("删除用户: userId={}", id);
        
        User user = getById(id);
        if (user == null) {
            log.warn("删除失败，用户不存在: userId={}", id);
            throw new BusinessException("用户不存在");
        }
        
        boolean result = removeById(id);
        if (result) {
            log.info("删除用户成功: userId={}, username={}", id, user.getUsername());
        } else {
            log.error("删除用户失败: userId={}", id);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBalance(Integer userId, BigDecimal amount) {
        log.info("更新账户余额: userId={}, amount={}", userId, amount);
        User user = getById(userId);
        if (user == null) {
            log.warn("更新余额失败，用户不存在: userId={}", userId);
            throw new BusinessException("用户不存在");
        }
        // 兜底处理，防止余额为null
        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }
        // 如果是扣款，需要验证余额是否足够
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            log.debug("扣款操作，验证余额是否足够: userId={}, currentBalance={}, amount={}", 
                    userId, user.getBalance(), amount);
            BigDecimal newBalance = user.getBalance().add(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                log.warn("扣款失败，余额不足: userId={}, currentBalance={}, amount={}", 
                        userId, user.getBalance(), amount);
                throw new BusinessException("余额不足");
            }
        }
        boolean result = baseMapper.updateBalance(userId, amount) > 0;
        if (result) {
            BigDecimal newBalance = user.getBalance().add(amount);
            log.info("更新账户余额成功: userId={}, oldBalance={}, newBalance={}, amount={}", 
                    userId, user.getBalance(), newBalance, amount);
        } else {
            log.error("更新账户余额失败: userId={}, amount={}", userId, amount);
        }
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(Integer userId, String status) {
        log.info("更新用户状态: userId={}, status={}", userId, status);
        
        User user = getById(userId);
        if (user == null) {
            log.warn("更新状态失败，用户不存在: userId={}", userId);
            throw new BusinessException("用户不存在");
        }
        
        // 验证状态值是否合法
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE") && !status.equals("BANNED")) {
            log.warn("更新状态失败，状态值不合法: status={}", status);
            throw new BusinessException("状态值不合法，应为ACTIVE、INACTIVE或BANNED");
        }
        
        // 更新用户状态
        User updateUser = new User();
        updateUser.setUserId(userId);
        updateUser.setStatus(status);
        
        boolean result = updateById(updateUser);
        if (result) {
            log.info("更新用户状态成功: userId={}, oldStatus={}, newStatus={}", 
                    userId, user.getStatus(), status);
        } else {
            log.error("更新用户状态失败: userId={}, status={}", userId, status);
        }
        
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword) {
        log.info("修改密码: userId={}", userId);
        
        // 验证用户是否存在
        User user = getById(userId);
        if (user == null) {
            log.warn("修改密码失败，用户不存在: userId={}", userId);
            throw new BusinessException("用户不存在");
        }
        
        // 验证旧密码是否正确
        String encryptedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!user.getPassword().equals(encryptedOldPassword)) {
            log.warn("修改密码失败，旧密码错误: userId={}", userId);
            throw new BusinessException("旧密码错误");
        }
        
        // 加密新密码
        String encryptedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        
        // 更新密码
        User updateUser = new User();
        updateUser.setUserId(userId);
        updateUser.setPassword(encryptedNewPassword);
        
        boolean result = updateById(updateUser);
        if (result) {
            log.info("修改密码成功: userId={}", userId);
        } else {
            log.error("修改密码失败: userId={}", userId);
        }
        
        return result;
    }
}
