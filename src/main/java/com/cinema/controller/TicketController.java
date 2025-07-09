package com.cinema.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cinema.common.result.Result;
import com.cinema.entity.Ticket;
import com.cinema.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.cinema.entity.User;
import com.cinema.service.UserService;
import com.cinema.entity.TicketVO;

/**
 * 票务控制器
 */
@Api(tags = "票务管理")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @ApiOperation("分页查询票务列表")
    @GetMapping("/page")
    public Result<Page<TicketVO>> pageTickets(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("用户ID") @RequestParam(required = false) Integer userId,
            @ApiParam("场次ID") @RequestParam(required = false) Integer showtimeId,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        Page<Ticket> page = new Page<>(current, size);
        Page<TicketVO> result = ticketService.pageTickets(page, userId, showtimeId, status);
        return Result.success(result);
    }

    @ApiOperation("获取票务详情")
    @GetMapping("/{id}")
    public Result<Ticket> getTicketById(@ApiParam("票ID") @PathVariable Integer id) {
        Ticket ticket = ticketService.getTicketById(id);
        return Result.success(ticket);
    }

    @ApiOperation("获取用户的票务列表")
    @GetMapping("/user/{userId}")
    public Result<List<Ticket>> getTicketsByUserId(@ApiParam("用户ID") @PathVariable Integer userId) {
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        return Result.success(tickets);
    }

    @ApiOperation("获取场次的票务列表")
    @GetMapping("/showtime/{showtimeId}")
    public Result<List<Ticket>> getTicketsByShowtimeId(@ApiParam("场次ID") @PathVariable Integer showtimeId) {
        List<Ticket> tickets = ticketService.getTicketsByShowtimeId(showtimeId);
        return Result.success(tickets);
    }

    @ApiOperation("手动选座购票")
    @PostMapping("/manual")
    public Result<List<Ticket>> manualSelectSeats(
            @ApiParam("用户ID") @RequestParam Integer userId,
            @ApiParam("场次ID") @RequestParam Integer showtimeId,
            @ApiParam("座位ID列表") @RequestBody List<Integer> seatIds) {
        List<Ticket> tickets = ticketService.manualSelectSeats(userId, showtimeId, seatIds);
        return Result.success(tickets);
    }

    @ApiOperation("自动选座购票")
    @PostMapping("/auto")
    public Result<List<Ticket>> autoSelectSeats(
            @ApiParam("用户ID") @RequestParam Integer userId,
            @ApiParam("场次ID") @RequestParam Integer showtimeId,
            @ApiParam("票数") @RequestParam Integer count) {
        List<Ticket> tickets = ticketService.autoSelectSeats(userId, showtimeId, count);
        return Result.success(tickets);
    }

    @ApiOperation("支付票款")
    @PostMapping("/{id}/pay")
    public Result<Map<String, Object>> payTicket(
            @ApiParam("票ID") @PathVariable Integer id,
            @ApiParam("支付方式") @RequestParam String paymentMethod) {
        boolean result = ticketService.payTicket(id, paymentMethod);
        if (result) {
            // 获取最新余额
            Ticket ticket = ticketService.getTicketById(id);
            User user = userService.getUserById(ticket.getUserId());
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("balance", user.getBalance());
            return Result.success(data);
        } else {
            return Result.error("支付失败");
        }
    }

    @ApiOperation("批量支付票款")
    @PostMapping("/batch-pay")
    public Result<Void> batchPayTickets(
            @ApiParam("票ID列表") @RequestBody List<Integer> ticketIds,
            @ApiParam("支付方式") @RequestParam String paymentMethod) {
        boolean result = ticketService.batchPayTickets(ticketIds, paymentMethod);
        if (result) {
            return Result.success();
        } else {
            return Result.error("支付失败");
        }
    }

    @ApiOperation("取消票务")
    @PutMapping("/{id}/cancel")
    public Result<Map<String, Object>> cancelTicket(@ApiParam("票ID") @PathVariable Integer id) {
        boolean result = ticketService.cancelTicket(id);
        if (result) {
            // 获取最新余额
            Ticket ticket = ticketService.getTicketById(id);
            User user = userService.getUserById(ticket.getUserId());
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("balance", user.getBalance());
            return Result.success(data);
        } else {
            return Result.error("取消失败");
        }
    }
    
    @ApiOperation("获取今日订单数")
    @GetMapping("/statistics/today-count")
    public Result<Integer> getTodayTicketCount() {
        int count = ticketService.getTodayTicketCount();
        return Result.success(count);
    }
    
    @ApiOperation("获取今日销售额")
    @GetMapping("/statistics/today-sales")
    public Result<BigDecimal> getTodaySales() {
        BigDecimal sales = ticketService.getTodaySales();
        return Result.success(sales);
    }
}
