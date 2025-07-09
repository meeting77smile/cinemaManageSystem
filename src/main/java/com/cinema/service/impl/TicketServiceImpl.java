package com.cinema.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cinema.common.exception.BusinessException;
import com.cinema.entity.Seat;
import com.cinema.entity.Showtime;
import com.cinema.entity.Ticket;
import com.cinema.entity.User;
import com.cinema.entity.TicketVO;
import com.cinema.entity.Showtime;
import com.cinema.entity.Movie;
import com.cinema.entity.Hall;
import com.cinema.entity.Cinema;
import com.cinema.mapper.TicketMapper;
import com.cinema.service.SeatService;
import com.cinema.service.ShowtimeService;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import com.cinema.service.HallService;
import com.cinema.service.CinemaService;
import com.cinema.util.SeatSelectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 票务服务实现类
 */
@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Autowired
    private UserService userService;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private com.cinema.service.MovieService movieService;

    @Autowired
    private HallService hallService;
    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private SeatSelectionUtil seatSelectionUtil;

    @Override
    public Page<TicketVO> pageTickets(Page<Ticket> page, Integer userId, Integer showtimeId, String status) {
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Ticket::getUserId, userId);
        }
        if (showtimeId != null) {
            wrapper.eq(Ticket::getShowtimeId, showtimeId);
        }
        if (status != null) {
            wrapper.eq(Ticket::getStatus, status);
        }
        wrapper.orderByDesc(Ticket::getPurchaseTime);
        Page<Ticket> ticketPage = page(page, wrapper);
        // 封装VO
        List<TicketVO> voList = ticketPage.getRecords().stream().map(ticket -> {
            TicketVO vo = new TicketVO();
            // 基本字段
            org.springframework.beans.BeanUtils.copyProperties(ticket, vo);
            try {
                Showtime showtime = showtimeService.getShowtimeById(ticket.getShowtimeId());
                vo.setStartTime(showtime.getStartTime() != null ? showtime.getStartTime().toString() : null);
                vo.setDuration(null);
                Movie movie = null;
                Hall hall = null;
                Cinema cinema = null;
                try { movie = movieService.getMovieById(showtime.getMovieId()); } catch (Exception e) {}
                try { hall = hallService.getHallById(showtime.getHallId()); } catch (Exception e) {}
                if (hall != null) {
                    try { cinema = cinemaService.getCinemaById(hall.getCinemaId()); } catch (Exception e) {}
                }
                vo.setMovieTitle(movie != null ? movie.getTitle() : "未知");
                vo.setHallName(hall != null ? hall.getName() : "未知");
                vo.setCinemaName(cinema != null ? cinema.getName() : "未知");
                vo.setDuration(movie != null ? movie.getDuration() : null);
                // 座位信息
                try {
                    Seat seat = seatService.getSeatById(ticket.getSeatId());
                    vo.setSeatRow(seat.getSeatRow());
                    vo.setSeatNumber(seat.getSeatNumber());
                } catch (Exception e) {}
            } catch (Exception e) {
                vo.setMovieTitle("未知");
                vo.setHallName("未知");
                vo.setCinemaName("未知");
            }
            return vo;
        }).collect(Collectors.toList());
        Page<TicketVO> voPage = new Page<>();
        voPage.setCurrent(ticketPage.getCurrent());
        voPage.setSize(ticketPage.getSize());
        voPage.setTotal(ticketPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public Ticket getTicketById(Integer id) {
        Ticket ticket = getById(id);
        if (ticket == null) {
            throw new BusinessException("票务不存在");
        }
        return ticket;
    }

    @Override
    public List<Ticket> getTicketsByUserId(Integer userId) {
        // 验证用户是否存在
        userService.getUserById(userId);
        
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ticket::getUserId, userId);
        wrapper.orderByDesc(Ticket::getPurchaseTime);
        return list(wrapper);
    }

    @Override
    public List<Ticket> getTicketsByShowtimeId(Integer showtimeId) {
        // 验证场次是否存在
        showtimeService.getShowtimeById(showtimeId);
        
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ticket::getShowtimeId, showtimeId);
        wrapper.orderByAsc(Ticket::getSeatId);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Ticket> manualSelectSeats(Integer userId, Integer showtimeId, List<Integer> seatIds) {
        // 验证用户是否存在
        User user = userService.getUserById(userId);
        
        // 验证场次是否存在
        Showtime showtime = showtimeService.getShowtimeById(showtimeId);
        
        // 验证场次是否已开始
        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("该场次已开始，无法购票");
        }
        
        // 验证场次票数是否足够
        if (showtime.getAvailableTickets() < seatIds.size()) {
            throw new BusinessException("可用票数不足");
        }
        
        // 验证座位是否存在且可用
        List<Seat> seats = new ArrayList<>();
        for (Integer seatId : seatIds) {
            Seat seat = seatService.getSeatById(seatId);
            
            // 验证座位是否属于该场次的影厅
            if (!seat.getHallId().equals(showtime.getHallId())) {
                throw new BusinessException("座位不属于该场次的影厅");
            }
            
            // 验证座位是否已被占用
            LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Ticket::getShowtimeId, showtimeId)
                    .eq(Ticket::getSeatId, seatId)
                    .ne(Ticket::getStatus, "CANCELED");
            if (count(wrapper) > 0) {
                throw new BusinessException("座位已被占用");
            }
            
            seats.add(seat);
        }
        
        // 创建票务
        List<Ticket> tickets = new ArrayList<>();
        for (Seat seat : seats) {
            Ticket ticket = new Ticket();
            ticket.setShowtimeId(showtimeId);
            ticket.setSeatId(seat.getSeatId());
            ticket.setUserId(userId);
            ticket.setPrice(showtime.getTicketPrice());
            ticket.setPurchaseTime(LocalDateTime.now());
            ticket.setStatus("UNPAID");
            tickets.add(ticket);
        }
        
        // 保存票务
        saveBatch(tickets);
        
        // 更新场次可用票数
        showtimeService.updateAvailableTickets(showtimeId, seatIds.size());
        
        return tickets;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Ticket> autoSelectSeats(Integer userId, Integer showtimeId, Integer count) {
        // 验证用户是否存在
        User user = userService.getUserById(userId);
        
        // 验证场次是否存在
        Showtime showtime = showtimeService.getShowtimeById(showtimeId);
        
        // 验证场次是否已开始
        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("该场次已开始，无法购票");
        }
        
        // 验证场次票数是否足够
        if (showtime.getAvailableTickets() < count) {
            throw new BusinessException("可用票数不足");
        }
        
        // 获取可用座位
        List<Seat> availableSeats = seatService.getAvailableSeatsByShowtime(showtimeId);
        
        // 自动选座
        List<Seat> selectedSeats = seatSelectionUtil.autoSelectSeats(availableSeats, count, 10, 10);
        if (selectedSeats.isEmpty()) {
            throw new BusinessException("无法找到合适的座位");
        }
        
        // 创建票务
        List<Ticket> tickets = new ArrayList<>();
        for (Seat seat : selectedSeats) {
            Ticket ticket = new Ticket();
            ticket.setShowtimeId(showtimeId);
            ticket.setSeatId(seat.getSeatId());
            ticket.setUserId(userId);
            ticket.setPrice(showtime.getTicketPrice());
            ticket.setPurchaseTime(LocalDateTime.now());
            ticket.setStatus("UNPAID");
            tickets.add(ticket);
        }
        
        // 保存票务
        saveBatch(tickets);
        
        // 更新场次可用票数
        showtimeService.updateAvailableTickets(showtimeId, count);
        
        return tickets;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payTicket(Integer ticketId, String paymentMethod) {
        Ticket ticket = getById(ticketId);
        if (ticket == null) {
            throw new BusinessException("票务不存在");
        }
        Showtime showtime = showtimeService.getShowtimeById(ticket.getShowtimeId());
        if (showtime == null) {
            throw new BusinessException("票务关联的场次不存在");
        }
        if (showtime.getMovieId() == null) {
            throw new BusinessException("票务关联的电影ID为空");
        }
        com.cinema.entity.Movie movie = null;
        try {
            movie = movieService.getMovieById(showtime.getMovieId());
        } catch (Exception e) {
            throw new BusinessException("票务关联的电影不存在");
        }
        if (!"UNPAID".equals(ticket.getStatus())) {
            throw new BusinessException("票务状态不正确");
        }
        User user = userService.getUserById(ticket.getUserId());
        if (user.getBalance() == null) {
            throw new BusinessException("用户余额异常");
        }
        if (ticket.getPrice() == null || ticket.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("票价异常");
        }
        if (user.getBalance().compareTo(ticket.getPrice()) < 0) {
            throw new BusinessException("余额不足");
        }
        boolean balanceResult = userService.updateBalance(user.getUserId(), ticket.getPrice().negate());
        if (!balanceResult) {
            throw new BusinessException("扣款失败，请重试");
        }
        ticket.setStatus("PAID");
        ticket.setPaymentMethod(paymentMethod);
        return updateById(ticket);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchPayTickets(List<Integer> ticketIds, String paymentMethod) {
        // 验证票务是否存在
        List<Ticket> tickets = listByIds(ticketIds);
        if (tickets.size() != ticketIds.size()) {
            throw new BusinessException("部分票务不存在");
        }
        
        // 验证票务状态
        for (Ticket ticket : tickets) {
            if (!"UNPAID".equals(ticket.getStatus())) {
                throw new BusinessException("部分票务状态不正确");
            }
        }
        
        // 计算总价
        BigDecimal totalPrice = tickets.stream()
                .map(Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 验证用户余额是否足够
        Integer userId = tickets.get(0).getUserId();
        User user = userService.getUserById(userId);
        if (user.getBalance().compareTo(totalPrice) < 0) {
            throw new BusinessException("余额不足");
        }
        
        // 扣除用户余额
        userService.updateBalance(userId, totalPrice.negate());
        
        // 更新票务状态
        for (Ticket ticket : tickets) {
            ticket.setStatus("PAID");
            ticket.setPaymentMethod(paymentMethod);
        }
        return updateBatchById(tickets);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTicket(Integer ticketId) {
        Ticket ticket = getById(ticketId);
        if (ticket == null) {
            throw new BusinessException("票务不存在");
        }
        if ("CANCELED".equals(ticket.getStatus())) {
            throw new BusinessException("票务已取消");
        }
        // 如果票务已支付，退还用户余额
        if ("PAID".equals(ticket.getStatus())) {
            if (ticket.getPrice() == null || ticket.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("票价异常");
            }
            boolean balanceResult = userService.updateBalance(ticket.getUserId(), ticket.getPrice());
            if (!balanceResult) {
                throw new BusinessException("退款失败，请重试");
            }
        }
        ticket.setStatus("CANCELED");
        boolean result = updateById(ticket);
        showtimeService.updateAvailableTickets(ticket.getShowtimeId(), -1);
        // 新增：释放座位
        if (ticket.getSeatId() != null) {
            Seat seat = seatService.getSeatById(ticket.getSeatId());
            seat.setIsAvailable(true);
            seatService.updateSeat(seat);
        }
        return result;
    }
    
    @Override
    public int getTodayTicketCount() {
        // 获取今天的开始和结束时间
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        // 查询今日已支付的票务数量
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ticket::getStatus, "PAID")
               .between(Ticket::getPurchaseTime, startOfDay, endOfDay);
        
        return (int)count(wrapper);
    }
    
    @Override
    public BigDecimal getTodaySales() {
        // 获取今天的开始和结束时间
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        // 查询今日已支付的票务
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ticket::getStatus, "PAID")
               .between(Ticket::getPurchaseTime, startOfDay, endOfDay);
        
        List<Ticket> tickets = list(wrapper);
        
        // 计算总销售额
        return tickets.stream()
                .map(Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
