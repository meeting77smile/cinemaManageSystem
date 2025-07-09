package com.cinema.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cinema.common.result.Result;
import com.cinema.entity.Movie;
import com.cinema.entity.Showtime;
import com.cinema.service.MovieService;
import com.cinema.service.ShowtimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电影控制器
 */
@Api(tags = "电影管理")
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @ApiOperation("获取所有电影列表")
    @GetMapping
    public Result<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.list();
        return Result.success(movies);
    }

    @ApiOperation("分页查询电影列表")
    @GetMapping("/page")
    public Result<Page<Movie>> pageMovies(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("电影标题") @RequestParam(required = false) String title) {
        Page<Movie> page = new Page<>(current, size);
        Page<Movie> result = movieService.pageMovies(page, title);
        return Result.success(result);
    }

    @ApiOperation("获取电影详情")
    @GetMapping("/{id}")
    public Result<Movie> getMovieById(@ApiParam("电影ID") @PathVariable Integer id) {
        Movie movie = movieService.getMovieById(id);
        return Result.success(movie);
    }

    @ApiOperation("获取正在上映的电影")
    @GetMapping("/showing")
    public Result<List<Movie>> getShowingMovies() {
        List<Movie> movies = movieService.getShowingMovies();
        return Result.success(movies);
    }

    @ApiOperation("获取即将下架的电影")
    @GetMapping("/ending")
    public Result<List<Movie>> getEndingMovies() {
        List<Movie> movies = movieService.getEndingMovies();
        return Result.success(movies);
    }

    @ApiOperation("获取电影的放映场次")
    @GetMapping("/{id}/showtimes")
    public Result<List<Showtime>> getShowtimesByMovieId(@ApiParam("电影ID") @PathVariable Integer id) {
        List<Showtime> showtimes = showtimeService.getShowtimesByMovieId(id);
        return Result.success(showtimes);
    }

    @ApiOperation("新增电影")
    @PostMapping
    public Result<Movie> saveMovie(@RequestBody Movie movie) {
        boolean result = movieService.saveMovie(movie);
        if (result) {
            return Result.success(movie);
        } else {
            return Result.error("新增失败");
        }
    }

    @ApiOperation("更新电影")
    @PutMapping
    public Result<Void> updateMovie(@RequestBody Movie movie) {
        boolean result = movieService.updateMovie(movie);
        if (result) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    @ApiOperation("删除电影")
    @DeleteMapping("/{id}")
    public Result<Void> removeMovie(@ApiParam("电影ID") @PathVariable Integer id) {
        boolean result = movieService.removeMovie(id);
        if (result) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
}
