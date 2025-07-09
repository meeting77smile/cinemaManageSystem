package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.Hall;
import org.apache.ibatis.annotations.Mapper;

/**
 * 影厅Mapper接口
 */
@Mapper
public interface HallMapper extends BaseMapper<Hall> {
}
