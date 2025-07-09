package com.cinema.controller;

import com.cinema.common.result.Result;
import com.cinema.entity.Seat;
import com.cinema.service.SeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 座位控制器
 */
@Api(tags = "座位管理")
@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @ApiOperation("获取座位详情")
    @GetMapping("/{id}")
    public Result<Seat> getSeatById(@ApiParam("座位ID") @PathVariable Integer id) {
        Seat seat = seatService.getSeatById(id);
        return Result.success(seat);
    }

    @ApiOperation("获取影厅的座位列表")
    @GetMapping("/hall/{hallId}")
    public Result<List<Seat>> getSeatsByHallId(@ApiParam("影厅ID") @PathVariable Integer hallId) {
        List<Seat> seats = seatService.getSeatsByHallId(hallId);
        return Result.success(seats);
    }

    @ApiOperation("获取场次的可用座位")
    @GetMapping("/showtime/{showtimeId}")
    public Result<List<Seat>> getAvailableSeatsByShowtime(@ApiParam("场次ID") @PathVariable Integer showtimeId) {
        List<Seat> seats = seatService.getAvailableSeatsByShowtime(showtimeId);
        return Result.success(seats);
    }

    @ApiOperation("新增座位")
    @PostMapping
    public Result<Seat> saveSeat(@RequestBody Seat seat) {
        boolean result = seatService.saveSeat(seat);
        if (result) {
            return Result.success(seat);
        } else {
            return Result.error("新增失败");
        }
    }

    @ApiOperation("更新座位")
    @PutMapping
    public Result<Void> updateSeat(@RequestBody Seat seat) {
        boolean result = seatService.updateSeat(seat);
        if (result) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    @ApiOperation("删除座位")
    @DeleteMapping("/{id}")
    public Result<Void> removeSeat(@ApiParam("座位ID") @PathVariable Integer id) {
        boolean result = seatService.removeSeat(id);
        if (result) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @ApiOperation("自动选座")
    @GetMapping("/auto-select")
    public Result<Seat> autoSelectSeat(
            @ApiParam("场次ID") @RequestParam Integer showtimeId) {
        Seat seat = seatService.autoSelectSeat(showtimeId);
        if (seat != null) {
            return Result.success(seat);
        } else {
            return Result.error("没有可用座位");
        }
    }
}
