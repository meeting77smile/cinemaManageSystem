package com.cinema.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cinema.entity.Hall;

import java.util.List;

/**
 * 影厅服务接口
 */
public interface HallService extends IService<Hall> {

    /**
     * 分页查询影厅列表
     *
     * @param page 分页参数
     * @param cinemaId 影院ID
     * @param name 影厅名称
     * @return 分页结果
     */
    Page<Hall> pageHalls(Page<Hall> page, Integer cinemaId, String name);

    /**
     * 获取影厅详情
     *
     * @param id 影厅ID
     * @return 影厅详情
     */
    Hall getHallById(Integer id);

    /**
     * 获取影院的所有影厅
     *
     * @param cinemaId 影院ID
     * @return 影厅列表
     */
    List<Hall> getHallsByCinemaId(Integer cinemaId);

    /**
     * 新增影厅
     *
     * @param hall 影厅信息
     * @return 是否成功
     */
    boolean saveHall(Hall hall);

    /**
     * 更新影厅
     *
     * @param hall 影厅信息
     * @return 是否成功
     */
    boolean updateHall(Hall hall);

    /**
     * 删除影厅
     *
     * @param id 影厅ID
     * @return 是否成功
     */
    boolean removeHall(Integer id);
}
