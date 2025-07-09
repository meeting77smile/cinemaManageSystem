package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 影厅实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hall")
public class Hall extends BaseEntity {

    /**
     * 影厅ID
     */
    @TableId(value = "hall_id", type = IdType.AUTO)
    private Integer hallId;

    /**
     * 所属影院ID
     */
    private Integer cinemaId;

    /**
     * 影厅名称
     */
    private String name;

    /**
     * 座位容量
     */
    private Integer capacity;

    /**
     * 可用座位数
     */
    private Integer availableSeats;

    /**
     * 影厅类型
     */
    private String type;
}
