package com.cinema.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cinema.common.exception.BusinessException;
import com.cinema.entity.Movie;
import com.cinema.mapper.MovieMapper;
import com.cinema.service.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 电影服务实现类
 */
@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Override
    public Page<Movie> pageMovies(Page<Movie> page, String title) {
        LambdaQueryWrapper<Movie> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(title)) {
            wrapper.like(Movie::getTitle, title);
        }
        wrapper.orderByDesc(Movie::getReleaseDate);
        return page(page, wrapper);
    }

    @Override
    public Movie getMovieById(Integer id) {
        Movie movie = getById(id);
        if (movie == null) {
            throw new BusinessException("电影不存在");
        }
        return movie;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMovie(Movie movie) {
        if (movie.getStatus() == null) {
            movie.setStatus("SHOWING");
        }
        if (movie.getDescription() == null) {
            movie.setDescription("");
        }
        return save(movie);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMovie(Movie movie) {
        Movie existMovie = getById(movie.getMovieId());
        if (existMovie == null) {
            throw new BusinessException("电影不存在");
        }
        return updateById(movie);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMovie(Integer id) {
        Movie movie = getById(id);
        if (movie == null) {
            throw new BusinessException("电影不存在");
        }
        return removeById(id);
    }

    @Override
    public List<Movie> getShowingMovies() {
        LambdaQueryWrapper<Movie> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Movie::getStatus, "SHOWING");
        wrapper.orderByDesc(Movie::getReleaseDate);
        return list(wrapper);
    }

    @Override
    public List<Movie> getEndingMovies() {
        LambdaQueryWrapper<Movie> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Movie::getStatus, "ENDING");
        wrapper.orderByDesc(Movie::getReleaseDate);
        return list(wrapper);
    }
}
