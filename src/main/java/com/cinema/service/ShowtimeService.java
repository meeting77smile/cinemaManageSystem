package com.cinema.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cinema.entity.Showtime;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 放映场次服务接口
 */
public interface ShowtimeService extends IService<Showtime> {

    /**
     * 分页查询放映场次列表
     *
     * @param page 分页参数
     * @param movieId 电影ID
     * @param hallId 影厅ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    Page<Showtime> pageShowtimes(Page<Showtime> page, Integer movieId, Integer hallId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取放映场次详情
     *
     * @param id 放映场次ID
     * @return 放映场次详情
     */
    Showtime getShowtimeById(Integer id);

    /**
     * 获取电影的放映场次
     *
     * @param movieId 电影ID
     * @return 放映场次列表
     */
    List<Showtime> getShowtimesByMovieId(Integer movieId);

    /**
     * 获取影厅的放映场次
     *
     * @param hallId 影厅ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 放映场次列表
     */
    List<Showtime> getShowtimesByHallId(Integer hallId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 新增放映场次
     *
     * @param showtime 放映场次信息
     * @return 是否成功
     */
    boolean saveShowtime(Showtime showtime);

    /**
     * 更新放映场次
     *
     * @param showtime 放映场次信息
     * @return 是否成功
     */
    boolean updateShowtime(Showtime showtime);

    /**
     * 删除放映场次
     *
     * @param id 放映场次ID
     * @return 是否成功
     */
    boolean removeShowtime(Integer id);

    /**
     * 更新场次可用票数
     *
     * @param showtimeId 场次ID
     * @param count 数量
     * @return 是否成功
     */
    boolean updateAvailableTickets(Integer showtimeId, Integer count);
}
