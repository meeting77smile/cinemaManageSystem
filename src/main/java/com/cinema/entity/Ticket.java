package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 票务实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ticket")
public class Ticket extends BaseEntity {

    /**
     * 票ID
     */
    @TableId(value = "ticket_id", type = IdType.AUTO)
    private Integer ticketId;

    /**
     * 场次ID
     */
    private Integer showtimeId;

    /**
     * 座位ID
     */
    private Integer seatId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 购买时间
     */
    private LocalDateTime purchaseTime;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 状态
     */
    private String status;
}
