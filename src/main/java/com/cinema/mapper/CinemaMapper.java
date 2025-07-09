package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.Cinema;
import org.apache.ibatis.annotations.Mapper;

/**
 * 影院Mapper接口
 */
@Mapper
public interface CinemaMapper extends BaseMapper<Cinema> {
}
