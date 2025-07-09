package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.Movie;
import org.apache.ibatis.annotations.Mapper;

/**
 * 电影Mapper接口
 */
@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
}
