package com.cinema.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cinema.entity.Seat;

import java.util.List;
import java.util.Map;

/**
 * 座位服务接口
 */
public interface SeatService extends IService<Seat> {

    /**
     * 获取影厅的所有座位
     *
     * @param hallId 影厅ID
     * @return 座位列表
     */
    List<Seat> getSeatsByHallId(Integer hallId);

    /**
     * 获取场次的可用座位
     *
     * @param showtimeId 场次ID
     * @return 可用座位列表
     */
    List<Seat> getAvailableSeatsByShowtime(Integer showtimeId);

    /**
     * 获取座位详情
     *
     * @param id 座位ID
     * @return 座位详情
     */
    Seat getSeatById(Integer id);

    /**
     * 新增座位
     *
     * @param seat 座位信息
     * @return 是否成功
     */
    boolean saveSeat(Seat seat);

    /**
     * 批量新增座位
     *
     * @param hallId 影厅ID
     * @param rows 行数
     * @param cols 列数
     * @return 是否成功
     */
    boolean batchSaveSeats(Integer hallId, Integer rows, Integer cols);

    /**
     * 更新座位
     *
     * @param seat 座位信息
     * @return 是否成功
     */
    boolean updateSeat(Seat seat);

    /**
     * 删除座位
     *
     * @param id 座位ID
     * @return 是否成功
     */
    boolean removeSeat(Integer id);
    
    /**
     * 锁定选中座位
     *
     * @param showtimeId 场次ID
     * @param seatIds 座位ID列表
     * @return 锁定结果，key为座位ID，value为是否锁定成功
     */
    Map<Integer, Boolean> lockSeats(Integer showtimeId, List<Integer> seatIds);

    /**
     * 自动选座
     */
    Seat autoSelectSeat(Integer showtimeId);
}
