package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.Showtime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 放映场次Mapper接口
 */
@Mapper
public interface ShowtimeMapper extends BaseMapper<Showtime> {

    /**
     * 更新场次可用票数
     *
     * @param showtimeId 场次ID
     * @param count 数量
     * @return 影响行数
     */
    @Update("UPDATE showtime SET available_tickets = available_tickets - #{count} WHERE showtime_id = #{showtimeId} AND available_tickets >= #{count}")
    int updateAvailableTickets(@Param("showtimeId") Integer showtimeId, @Param("count") Integer count);
}
