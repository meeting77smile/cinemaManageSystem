package com.cinema.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cinema.common.result.Result;
import com.cinema.entity.Seat;
import com.cinema.entity.Showtime;
import com.cinema.entity.Ticket;
import com.cinema.entity.ShowtimeVO;
import com.cinema.entity.Movie;
import com.cinema.entity.Hall;
import com.cinema.entity.Cinema;
import com.cinema.service.SeatService;
import com.cinema.service.ShowtimeService;
import com.cinema.service.TicketService;
import com.cinema.service.MovieService;
import com.cinema.service.HallService;
import com.cinema.service.CinemaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 放映场次控制器
 */
@Api(tags = "放映场次管理")
@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private SeatService seatService;
    
    @Autowired
    private TicketService ticketService;

    @Autowired
    private MovieService movieService;
    @Autowired
    private HallService hallService;
    @Autowired
    private CinemaService cinemaService;

    @ApiOperation("分页查询放映场次列表")
    @GetMapping("/page")
    public Result<Page<ShowtimeVO>> pageShowtimes(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("电影ID") @RequestParam(required = false) Integer movieId,
            @ApiParam("影厅ID") @RequestParam(required = false) Integer hallId,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Page<Showtime> page = new Page<>(current, size);
        Page<Showtime> result = showtimeService.pageShowtimes(page, movieId, hallId, startTime, endTime);
        // 组装VO
        List<ShowtimeVO> voList = result.getRecords().stream().map(showtime -> {
            ShowtimeVO vo = new ShowtimeVO();
            BeanUtils.copyProperties(showtime, vo);
            Movie movie = null;
            try {
                movie = movieService.getMovieById(showtime.getMovieId());
            } catch (Exception e) {
                // 电影不存在，movie为null
            }
            Hall hall = null;
            try {
                hall = hallService.getHallById(showtime.getHallId());
            } catch (Exception e) {
                // 影厅不存在，hall为null
            }
            String cinemaName = "";
            if (hall != null) {
                Cinema cinema = null;
                try {
                    cinema = cinemaService.getCinemaById(hall.getCinemaId());
                } catch (Exception e) {
                    // 影院不存在
                }
                cinemaName = cinema != null ? cinema.getName() : "";
                vo.setHallName(hall.getName());
            }
            vo.setMovieTitle(movie != null ? movie.getTitle() : "");
            vo.setCinemaName(cinemaName);
            return vo;
        }).collect(Collectors.toList());
        // 构造VO分页对象
        Page<ShowtimeVO> voPage = new Page<>();
        voPage.setCurrent(result.getCurrent());
        voPage.setSize(result.getSize());
        voPage.setTotal(result.getTotal());
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @ApiOperation("获取放映场次详情")
    @GetMapping("/{id}")
    public Result<ShowtimeVO> getShowtimeById(@ApiParam("场次ID") @PathVariable Integer id) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        ShowtimeVO vo = new ShowtimeVO();
        org.springframework.beans.BeanUtils.copyProperties(showtime, vo);
        Movie movie = movieService.getMovieById(showtime.getMovieId());
        Hall hall = hallService.getHallById(showtime.getHallId());
        String cinemaName = "";
        if (hall != null) {
            Cinema cinema = cinemaService.getCinemaById(hall.getCinemaId());
            cinemaName = cinema != null ? cinema.getName() : "";
            vo.setHallName(hall.getName());
        }
        vo.setMovieTitle(movie != null ? movie.getTitle() : "");
        vo.setCinemaName(cinemaName);
        vo.setDuration(movie != null ? movie.getDuration() : null);
        return Result.success(vo);
    }

    @ApiOperation("获取电影的放映场次")
    @GetMapping("/movie/{movieId}")
    public Result<List<Showtime>> getShowtimesByMovieId(@ApiParam("电影ID") @PathVariable Integer movieId) {
        List<Showtime> showtimes = showtimeService.getShowtimesByMovieId(movieId);
        return Result.success(showtimes);
    }

    @ApiOperation("获取影厅的放映场次")
    @GetMapping("/hall/{hallId}")
    public Result<List<Showtime>> getShowtimesByHallId(
            @ApiParam("影厅ID") @PathVariable Integer hallId,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<Showtime> showtimes = showtimeService.getShowtimesByHallId(hallId, startTime, endTime);
        return Result.success(showtimes);
    }

    @ApiOperation("获取场次的可用座位")
    @GetMapping("/{id}/seats")
    public Result<List<Seat>> getAvailableSeatsByShowtime(@ApiParam("场次ID") @PathVariable Integer id) {
        List<Seat> seats = seatService.getAvailableSeatsByShowtime(id);
        return Result.success(seats);
    }

    @ApiOperation("新增放映场次")
    @PostMapping
    public Result<Showtime> saveShowtime(@RequestBody Showtime showtime) {
        boolean result = showtimeService.saveShowtime(showtime);
        if (result) {
            return Result.success(showtime);
        } else {
            return Result.error("新增失败");
        }
    }

    @ApiOperation("更新放映场次")
    @PutMapping
    public Result<Void> updateShowtime(@RequestBody Showtime showtime) {
        boolean result = showtimeService.updateShowtime(showtime);
        if (result) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    @ApiOperation("删除放映场次")
    @DeleteMapping("/{id}")
    public Result<Void> removeShowtime(@ApiParam("场次ID") @PathVariable Integer id) {
        boolean result = showtimeService.removeShowtime(id);
        if (result) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
    
    @ApiOperation("锁定选中座位")
    @PostMapping("/{showtimeId}/seats/lock")
    public Result<Map<Integer, Boolean>> lockSeats(
            @ApiParam("场次ID") @PathVariable Integer showtimeId,
            @ApiParam("座位ID列表") @RequestBody List<Integer> seatIds) {
        Map<Integer, Boolean> lockResult = seatService.lockSeats(showtimeId, seatIds);
        return Result.success(lockResult);
    }
    
    @ApiOperation("自动选座")
    @PostMapping("/{showtimeId}/seats/auto-select")
    public Result<List<Ticket>> autoSelectSeats(
            @ApiParam("场次ID") @PathVariable Integer showtimeId,
            @ApiParam("用户ID") @RequestParam Integer userId,
            @ApiParam("票数") @RequestParam Integer count) {
        List<Ticket> tickets = ticketService.autoSelectSeats(userId, showtimeId, count);
        return Result.success(tickets);
    }
}
