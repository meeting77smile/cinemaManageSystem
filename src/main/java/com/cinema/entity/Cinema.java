package com.cinema.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 影院实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cinema")
public class Cinema extends BaseEntity {

    /**
     * 影院ID
     */
    @TableId(value = "cinema_id", type = IdType.AUTO)
    private Integer cinemaId;

    /**
     * 影院名称
     */
    private String name;

    /**
     * 影院地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String contact;

    /**
     * 总厅数
     */
    private Integer totalHalls;
}
