package com.cinema.controller;

import com.cinema.common.result.Result;
import com.cinema.entity.User;
import com.cinema.service.CinemaService;
import com.cinema.service.MovieService;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 */
@Api(tags = "管理员管理")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CinemaService cinemaService;
    
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private TicketService ticketService;
    
    @ApiOperation("获取系统统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 用户总数
        statistics.put("userCount", userService.count());
        
        // 影院总数
        statistics.put("cinemaCount", cinemaService.count());
        
        // 电影总数
        statistics.put("movieCount", movieService.count());
        
        // 订单总数
        statistics.put("ticketCount", ticketService.count());
        
        // 今日订单数
        statistics.put("todayTicketCount", ticketService.getTodayTicketCount());
        
        // 今日销售额
        statistics.put("todaySales", ticketService.getTodaySales());
        
        return Result.success(statistics);
    }
    
    @ApiOperation("获取所有用户")
    @GetMapping("/users")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }
    
    @ApiOperation("修改用户状态")
    @PutMapping("/users/{userId}/status")
    public Result<Void> updateUserStatus(
            @ApiParam("用户ID") @PathVariable Integer userId,
            @ApiParam("状态") @RequestParam String status) {
        boolean result = userService.updateUserStatus(userId, status);
        if (result) {
            return Result.success();
        } else {
            return Result.error("修改用户状态失败");
        }
    }
}
