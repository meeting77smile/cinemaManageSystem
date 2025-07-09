package com.cinema.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cinema.entity.Seat;
import com.cinema.entity.Ticket;
import com.cinema.entity.TicketVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 票务服务接口
 */
public interface TicketService extends IService<Ticket> {

    /**
     * 分页查询票务列表
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param showtimeId 场次ID
     * @param status 状态
     * @return 分页结果
     */
    Page<TicketVO> pageTickets(Page<Ticket> page, Integer userId, Integer showtimeId, String status);

    /**
     * 获取票务详情
     *
     * @param id 票ID
     * @return 票务详情
     */
    Ticket getTicketById(Integer id);

    /**
     * 获取用户的票务列表
     *
     * @param userId 用户ID
     * @return 票务列表
     */
    List<Ticket> getTicketsByUserId(Integer userId);

    /**
     * 获取场次的票务列表
     *
     * @param showtimeId 场次ID
     * @return 票务列表
     */
    List<Ticket> getTicketsByShowtimeId(Integer showtimeId);

    /**
     * 手动选座购票
     *
     * @param userId 用户ID
     * @param showtimeId 场次ID
     * @param seatIds 座位ID列表
     * @return 票务列表
     */
    List<Ticket> manualSelectSeats(Integer userId, Integer showtimeId, List<Integer> seatIds);

    /**
     * 自动选座购票
     *
     * @param userId 用户ID
     * @param showtimeId 场次ID
     * @param count 票数
     * @return 票务列表
     */
    List<Ticket> autoSelectSeats(Integer userId, Integer showtimeId, Integer count);

    /**
     * 支付票款
     *
     * @param ticketId 票ID
     * @param paymentMethod 支付方式
     * @return 是否成功
     */
    boolean payTicket(Integer ticketId, String paymentMethod);

    /**
     * 批量支付票款
     *
     * @param ticketIds 票ID列表
     * @param paymentMethod 支付方式
     * @return 是否成功
     */
    boolean batchPayTickets(List<Integer> ticketIds, String paymentMethod);

    /**
     * 取消票务
     *
     * @param ticketId 票ID
     * @return 是否成功
     */
    boolean cancelTicket(Integer ticketId);
    
    /**
     * 获取今日订单数
     *
     * @return 今日订单数
     */
    int getTodayTicketCount();
    
    /**
     * 获取今日销售额
     *
     * @return 今日销售额
     */
    BigDecimal getTodaySales();
}
