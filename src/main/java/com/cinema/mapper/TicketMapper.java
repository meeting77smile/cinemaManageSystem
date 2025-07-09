package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 票务Mapper接口
 */
@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {

    /**
     * 更新票务状态
     *
     * @param ticketId 票ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE ticket SET status = #{status} WHERE ticket_id = #{ticketId}")
    int updateStatus(@Param("ticketId") Integer ticketId, @Param("status") String status);
}
