package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 放映场次实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("showtime")
public class Showtime extends BaseEntity {

    /**
     * 场次ID
     */
    @TableId(value = "showtime_id", type = IdType.AUTO)
    private Integer showtimeId;

    /**
     * 电影ID
     */
    private Integer movieId;

    /**
     * 影厅ID
     */
    private Integer hallId;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 票价
     */
    private BigDecimal ticketPrice;

    /**
     * 可用票数
     */
    private Integer availableTickets;

    /**
     * 状态
     */
    private String status;
}
