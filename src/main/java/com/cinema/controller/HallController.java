package com.cinema.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cinema.common.result.Result;
import com.cinema.entity.Hall;
import com.cinema.entity.Seat;
import com.cinema.entity.Showtime;
import com.cinema.service.HallService;
import com.cinema.service.SeatService;
import com.cinema.service.ShowtimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 影厅控制器
 */
@Api(tags = "影厅管理")
@RestController
@RequestMapping("/api/halls")
public class HallController {

    @Autowired
    private HallService hallService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private ShowtimeService showtimeService;

    @ApiOperation("分页查询影厅列表")
    @GetMapping("/page")
    public Result<Page<Hall>> pageHalls(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("影院ID") @RequestParam(required = false) Integer cinemaId,
            @ApiParam("影厅名称") @RequestParam(required = false) String name) {
        Page<Hall> page = new Page<>(current, size);
        Page<Hall> result = hallService.pageHalls(page, cinemaId, name);
        return Result.success(result);
    }

    @ApiOperation("获取影厅详情")
    @GetMapping("/{id}")
    public Result<Hall> getHallById(@ApiParam("影厅ID") @PathVariable Integer id) {
        Hall hall = hallService.getHallById(id);
        return Result.success(hall);
    }

    @ApiOperation("获取影厅的座位列表")
    @GetMapping("/{id}/seats")
    public Result<List<Seat>> getSeatsByHallId(@ApiParam("影厅ID") @PathVariable Integer id) {
        List<Seat> seats = seatService.getSeatsByHallId(id);
        return Result.success(seats);
    }

    @ApiOperation("获取影厅的放映场次")
    @GetMapping("/{id}/showtimes")
    public Result<List<Showtime>> getShowtimesByHallId(
            @ApiParam("影厅ID") @PathVariable Integer id,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<Showtime> showtimes = showtimeService.getShowtimesByHallId(id, startTime, endTime);
        return Result.success(showtimes);
    }

    @ApiOperation("新增影厅")
    @PostMapping
    public Result<Hall> saveHall(@RequestBody Hall hall) {
        boolean result = hallService.saveHall(hall);
        if (result) {
            return Result.success(hall);
        } else {
            return Result.error("新增失败");
        }
    }

    @ApiOperation("更新影厅")
    @PutMapping
    public Result<Void> updateHall(@RequestBody Hall hall) {
        boolean result = hallService.updateHall(hall);
        if (result) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    @ApiOperation("删除影厅")
    @DeleteMapping("/{id}")
    public Result<Void> removeHall(@ApiParam("影厅ID") @PathVariable Integer id) {
        boolean result = hallService.removeHall(id);
        if (result) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @ApiOperation("批量创建座位")
    @PostMapping("/{id}/seats")
    public Result<Void> batchSaveSeats(
            @ApiParam("影厅ID") @PathVariable Integer id,
            @ApiParam("行数") @RequestParam Integer rows,
            @ApiParam("列数") @RequestParam Integer cols) {
        boolean result = seatService.batchSaveSeats(id, rows, cols);
        if (result) {
            return Result.success();
        } else {
            return Result.error("创建失败");
        }
    }
}
