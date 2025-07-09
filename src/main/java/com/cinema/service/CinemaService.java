package com.cinema.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cinema.entity.Cinema;
import com.cinema.entity.CinemaReview;

import java.util.List;

/**
 * 影院服务接口
 */
public interface CinemaService extends IService<Cinema> {

    /**
     * 分页查询影院列表
     *
     * @param page 分页参数
     * @param name 影院名称
     * @return 分页结果
     */
    Page<Cinema> pageCinemas(Page<Cinema> page, String name);

    /**
     * 获取影院详情
     *
     * @param id 影院ID
     * @return 影院详情
     */
    Cinema getCinemaById(Integer id);

    /**
     * 新增影院
     *
     * @param cinema 影院信息
     * @return 是否成功
     */
    boolean saveCinema(Cinema cinema);

    /**
     * 更新影院
     *
     * @param cinema 影院信息
     * @return 是否成功
     */
    boolean updateCinema(Cinema cinema);

    /**
     * 删除影院
     *
     * @param id 影院ID
     * @return 是否成功
     */
    boolean removeCinema(Integer id);
    
    /**
     * 获取影院评价列表
     *
     * @param cinemaId 影院ID
     * @return 评价列表
     */
    List<CinemaReview> getCinemaReviews(Integer cinemaId);
    
    /**
     * 添加影院评价
     *
     * @param cinemaId 影院ID
     * @param review 评价信息
     * @return 是否成功
     */
    boolean addCinemaReview(Integer cinemaId, CinemaReview review);
}
