package com.cinema.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cinema.common.result.Result;
import com.cinema.entity.Cinema;
import com.cinema.entity.CinemaReview;
import com.cinema.entity.Hall;
import com.cinema.service.CinemaService;
import com.cinema.service.HallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 影院控制器
 */
@Api(tags = "影院管理")
@RestController
@RequestMapping("/api/cinemas")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private HallService hallService;

    @ApiOperation("获取所有影院列表")
    @GetMapping
    public Result<List<Cinema>> getAllCinemas() {
        List<Cinema> cinemas = cinemaService.list();
        return Result.success(cinemas);
    }

    @ApiOperation("分页查询影院列表")
    @GetMapping("/page")
    public Result<Page<Cinema>> pageCinemas(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("影院名称") @RequestParam(required = false) String name) {
        Page<Cinema> page = new Page<>(current, size);
        Page<Cinema> result = cinemaService.pageCinemas(page, name);
        return Result.success(result);
    }

    @ApiOperation("获取影院详情")
    @GetMapping("/{id}")
    public Result<Cinema> getCinemaById(@ApiParam("影院ID") @PathVariable Integer id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        return Result.success(cinema);
    }

    @ApiOperation("获取影院的影厅列表")
    @GetMapping("/{id}/halls")
    public Result<List<Hall>> getHallsByCinemaId(@ApiParam("影院ID") @PathVariable Integer id) {
        List<Hall> halls = hallService.getHallsByCinemaId(id);
        return Result.success(halls);
    }

    @ApiOperation("新增影院")
    @PostMapping
    public Result<Cinema> saveCinema(@RequestBody Cinema cinema) {
        boolean result = cinemaService.saveCinema(cinema);
        if (result) {
            return Result.success(cinema);
        } else {
            return Result.error("新增失败");
        }
    }

    @ApiOperation("更新影院")
    @PutMapping
    public Result<Void> updateCinema(@RequestBody Cinema cinema) {
        boolean result = cinemaService.updateCinema(cinema);
        if (result) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    @ApiOperation("删除影院")
    @DeleteMapping("/{id}")
    public Result<Void> removeCinema(@ApiParam("影院ID") @PathVariable Integer id) {
        boolean result = cinemaService.removeCinema(id);
        if (result) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
    
    @ApiOperation("获取影院评价")
    @GetMapping("/{cinemaId}/reviews")
    public Result<List<CinemaReview>> getCinemaReviews(@ApiParam("影院ID") @PathVariable Integer cinemaId) {
        List<CinemaReview> reviews = cinemaService.getCinemaReviews(cinemaId);
        return Result.success(reviews);
    }
    
    @ApiOperation("提交影院评价")
    @PostMapping("/{cinemaId}/reviews")
    public Result<CinemaReview> addCinemaReview(
            @ApiParam("影院ID") @PathVariable Integer cinemaId,
            @RequestBody CinemaReview review) {
        boolean result = cinemaService.addCinemaReview(cinemaId, review);
        if (result) {
            return Result.success(review);
        } else {
            return Result.error("提交评价失败");
        }
    }
}
