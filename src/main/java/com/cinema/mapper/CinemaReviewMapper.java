package com.cinema.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cinema.entity.CinemaReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 影院评价Mapper接口
 */
@Mapper
public interface CinemaReviewMapper extends BaseMapper<CinemaReview> {

    /**
     * 根据影院ID获取评价列表
     *
     * @param cinemaId 影院ID
     * @return 评价列表
     */
    @Select("SELECT r.*, u.username FROM cinema_review r LEFT JOIN user u ON r.user_id = u.user_id WHERE r.cinema_id = #{cinemaId} ORDER BY r.review_time DESC")
    List<CinemaReview> getReviewsByCinemaId(@Param("cinemaId") Integer cinemaId);
}
