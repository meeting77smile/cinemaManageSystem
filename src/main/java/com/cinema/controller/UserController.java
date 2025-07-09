package com.cinema.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cinema.common.result.Result;
import com.cinema.entity.*;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import com.cinema.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cinema.service.ShowtimeService;
import com.cinema.service.MovieService;
import com.cinema.service.HallService;
import com.cinema.service.CinemaService;
import com.cinema.service.SeatService;

/**
 * 用户控制器
 */
@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private TicketService ticketService;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private HallService hallService;

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private SeatService seatService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        log.info("用户注册请求: username={}", user.getUsername());
        boolean result = userService.register(user);
        if (result) {
            // 清空密码
            user.setPassword(null);
            log.info("用户注册成功: userId={}, username={}", user.getUserId(), user.getUsername());
            return Result.success(user);
        } else {
            log.error("用户注册失败: username={}", user.getUsername());
            return Result.error("注册失败");
        }
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password) {
        log.info("用户登录请求: username={}", username);
        
        User user = userService.login(username, password);
        
        // 生成token
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());
        
        // 返回用户信息和token
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        
        log.info("用户登录成功: userId={}, username={}", user.getUserId(), user.getUsername());
        return Result.success(map);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<User> getUserInfo(@ApiParam("用户ID") @RequestParam Integer userId) {
        log.info("获取用户信息请求: userId={}", userId);
        
        User user = userService.getUserById(userId);
        // 清空密码
        user.setPassword(null);
        
        log.info("获取用户信息成功: userId={}, username={}", user.getUserId(), user.getUsername());
        return Result.success(user);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody User user) {
        log.info("更新用户信息请求: userId={}", user.getUserId());
        
        boolean result = userService.updateUser(user);
        if (result) {
            log.info("更新用户信息成功: userId={}", user.getUserId());
            return Result.success();
        } else {
            log.error("更新用户信息失败: userId={}", user.getUserId());
            return Result.error("更新失败");
        }
    }

    @ApiOperation("账户充值")
    @PostMapping("/recharge")
    public Result<Map<String, Object>> recharge(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        BigDecimal amount = new BigDecimal(params.get("amount").toString());
        String paymentMethod = (String) params.get("paymentMethod");
        
        log.info("账户充值请求: userId={}, amount={}, paymentMethod={}", userId, amount, paymentMethod);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("充值金额无效: userId={}, amount={}", userId, amount);
            return Result.error("充值金额必须大于0");
        }
        
        boolean result = userService.updateBalance(userId, amount);
        if (result) {
            log.info("账户充值成功: userId={}, amount={}", userId, amount);
            
            // 获取更新后的用户信息
            User user = userService.getUserById(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("balance", user.getBalance());
            
            return Result.success(data);
        } else {
            log.error("账户充值失败: userId={}, amount={}", userId, amount);
            return Result.error("充值失败");
        }
    }

    @ApiOperation("分页查询用户列表")
    @GetMapping("/page")
    public Result<Page<User>> pageUsers(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("用户名") @RequestParam(required = false) String username) {
        log.info("分页查询用户列表请求: current={}, size={}, username={}", current, size, username);
        
        Page<User> page = new Page<>(current, size);
        Page<User> result = userService.pageUsers(page, username);
        
        // 清空密码
        result.getRecords().forEach(user -> user.setPassword(null));
        
        log.info("分页查询用户列表成功: 总记录数={}, 总页数={}", result.getTotal(), result.getPages());
        return Result.success(result);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> removeUser(@ApiParam("用户ID") @PathVariable Integer id) {
        log.info("删除用户请求: userId={}", id);
        
        boolean result = userService.removeUser(id);
        if (result) {
            log.info("删除用户成功: userId={}", id);
            return Result.success();
        } else {
            log.error("删除用户失败: userId={}", id);
            return Result.error("删除失败");
        }
    }
    
    @ApiOperation("获取用户订单列表")
    @GetMapping("/{userId}/tickets")
    public Result<List<TicketVO>> getUserTickets(@ApiParam("用户ID") @PathVariable Integer userId) {
        log.info("获取用户订单列表请求: userId={}", userId);
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        List<TicketVO> voList = tickets.stream().map(ticket -> {
            try {
                TicketVO vo = new TicketVO();
                org.springframework.beans.BeanUtils.copyProperties(ticket, vo);
                Showtime showtime = ticket.getShowtimeId() != null ? showtimeService.getShowtimeById(ticket.getShowtimeId()) : null;
                if (showtime == null) throw new RuntimeException("场次不存在");
                Movie movie = (showtime.getMovieId() != null) ? movieService.getMovieById(showtime.getMovieId()) : null;
                if (movie == null) throw new RuntimeException("电影不存在");
                Hall hall = (showtime.getHallId() != null) ? hallService.getHallById(showtime.getHallId()) : null;
                if (hall == null) throw new RuntimeException("影厅不存在");
                Cinema cinema = (hall.getCinemaId() != null) ? cinemaService.getCinemaById(hall.getCinemaId()) : null;
                if (cinema == null) throw new RuntimeException("影院不存在");
                Seat seat = (ticket.getSeatId() != null) ? seatService.getSeatById(ticket.getSeatId()) : null;
                if (seat == null) throw new RuntimeException("座位不存在");
                vo.setMovieTitle(movie.getTitle());
                vo.setDuration(movie.getDuration());
                vo.setCinemaName(cinema.getName());
                vo.setHallName(hall.getName());
                vo.setStartTime(showtime.getStartTime() != null ? showtime.getStartTime().toString() : "");
                vo.setSeatRow(seat.getSeatRow());
                vo.setSeatNumber(seat.getSeatNumber());
                return vo;
            } catch (Exception e) {
                log.warn("订单数据异常，ticketId={}, 原因={}", ticket.getTicketId(), e.getMessage());
                return null;
            }
        }).filter(java.util.Objects::nonNull).collect(java.util.stream.Collectors.toList());
        log.info("获取用户订单列表成功: userId={}, count={}", userId, voList.size());
        return Result.success(voList);
    }
    
    @ApiOperation("修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");
        
        log.info("修改密码请求: userId={}", userId);
        
        if (userId == null || oldPassword == null || newPassword == null) {
            log.warn("修改密码失败，参数不完整: userId={}", userId);
            return Result.error("参数不完整");
        }
        
        boolean result = userService.updatePassword(userId, oldPassword, newPassword);
        if (result) {
            log.info("修改密码成功: userId={}", userId);
            return Result.success();
        } else {
            log.error("修改密码失败: userId={}", userId);
            return Result.error("修改密码失败");
        }
    }
}
