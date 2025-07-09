package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 电影实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("movie")
public class Movie extends BaseEntity {

    /**
     * 电影ID
     */
    @TableId(value = "movie_id", type = IdType.AUTO)
    private Integer movieId;

    /**
     * 电影标题
     */
    private String title;

    /**
     * 导演
     */
    private String director;

    /**
     * 演员
     */
    private String actors;

    /**
     * 时长(分钟)
     */
    private Integer duration;

    /**
     * 上映日期
     */
    private LocalDate releaseDate;

    /**
     * 电影描述
     */
    private String description;

    /**
     * 海报URL
     */
    private String posterUrl;

    /**
     * 状态(SHOWING:上映中, ENDING:即将下架, ENDED:已下架)
     */
    private String status;
}
