package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 座位Mapper接口
 */
@Mapper
public interface SeatMapper extends BaseMapper<Seat> {

    /**
     * 获取场次的可用座位
     *
     * @param showtimeId 场次ID
     * @return 可用座位列表
     */
    @Select("SELECT s.* FROM seat s " +
            "LEFT JOIN ticket t ON s.seat_id = t.seat_id AND t.showtime_id = #{showtimeId} AND t.is_deleted = 0 AND t.status = 'PAID' " +
            "WHERE s.is_available = 1 AND s.is_deleted = 0 AND t.ticket_id IS NULL")
    List<Seat> getAvailableSeatsByShowtime(@Param("showtimeId") Integer showtimeId);
}
