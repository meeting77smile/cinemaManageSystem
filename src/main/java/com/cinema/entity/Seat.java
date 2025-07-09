package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 座位实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seat")
public class Seat extends BaseEntity {

    /**
     * 座位ID
     */
    @TableId(value = "seat_id", type = IdType.AUTO)
    private Integer seatId;

    /**
     * 所属影厅ID
     */
    private Integer hallId;

    /**
     * 排号
     */
    private Integer seatRow;

    /**
     * 座位号
     */
    private Integer seatNumber;

    /**
     * 是否可用
     */
    private Boolean isAvailable;

    /**
     * 座位类型
     */
    private String type;
}
