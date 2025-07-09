package com.cinema.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cinema.entity.Movie;

import java.util.List;

/**
 * 电影服务接口
 */
public interface MovieService extends IService<Movie> {

    /**
     * 分页查询电影列表
     *
     * @param page 分页参数
     * @param title 电影标题
     * @return 分页结果
     */
    Page<Movie> pageMovies(Page<Movie> page, String title);

    /**
     * 获取电影详情
     *
     * @param id 电影ID
     * @return 电影详情
     */
    Movie getMovieById(Integer id);

    /**
     * 新增电影
     *
     * @param movie 电影信息
     * @return 是否成功
     */
    boolean saveMovie(Movie movie);

    /**
     * 更新电影
     *
     * @param movie 电影信息
     * @return 是否成功
     */
    boolean updateMovie(Movie movie);

    /**
     * 删除电影
     *
     * @param id 电影ID
     * @return 是否成功
     */
    boolean removeMovie(Integer id);

    /**
     * 获取正在上映的电影
     *
     * @return 电影列表
     */
    List<Movie> getShowingMovies();

    /**
     * 获取即将下架的电影
     *
     * @return 电影列表
     */
    List<Movie> getEndingMovies();
}
