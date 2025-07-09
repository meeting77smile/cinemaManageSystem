package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 影院评价实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cinema_review")
@ApiModel(value = "影院评价", description = "影院评价信息")
public class CinemaReview extends BaseEntity {

    @ApiModelProperty("评价ID")
    @TableId(value = "review_id", type = IdType.AUTO)
    private Integer reviewId;

    @ApiModelProperty("影院ID")
    private Integer cinemaId;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("评分(1-5)")
    private Integer rating;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("评价时间")
    private LocalDateTime reviewTime;

    @ApiModelProperty("用户名")
    private String username;
}
