package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 更新用户余额
     *
     * @param userId 用户ID
     * @param amount 金额
     * @return 影响行数
     */
    @Update("UPDATE user SET balance = balance + #{amount} WHERE user_id = #{userId}")
    int updateBalance(@Param("userId") Integer userId, @Param("amount") BigDecimal amount);
}
