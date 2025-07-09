package com.cinema.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cinema.common.exception.BusinessException;
import com.cinema.entity.Cinema;
import com.cinema.entity.CinemaReview;
import com.cinema.mapper.CinemaMapper;
import com.cinema.mapper.CinemaReviewMapper;
import com.cinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 影院服务实现类
 */
@Service
public class CinemaServiceImpl extends ServiceImpl<CinemaMapper, Cinema> implements CinemaService {

    @Autowired
    private CinemaReviewMapper cinemaReviewMapper;

    @Override
    public Page<Cinema> pageCinemas(Page<Cinema> page, String name) {
        LambdaQueryWrapper<Cinema> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Cinema::getName, name);
        }
        wrapper.orderByDesc(Cinema::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Cinema getCinemaById(Integer id) {
        Cinema cinema = getById(id);
        if (cinema == null) {
            throw new BusinessException("影院不存在");
        }
        return cinema;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCinema(Cinema cinema) {
        // 设置默认值
        if (cinema.getTotalHalls() == null) {
            cinema.setTotalHalls(0);
        }
        return save(cinema);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCinema(Cinema cinema) {
        Cinema existCinema = getById(cinema.getCinemaId());
        if (existCinema == null) {
            throw new BusinessException("影院不存在");
        }
        return updateById(cinema);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCinema(Integer id) {
        Cinema cinema = getById(id);
        if (cinema == null) {
            throw new BusinessException("影院不存在");
        }
        return removeById(id);
    }
    
    @Override
    public List<CinemaReview> getCinemaReviews(Integer cinemaId) {
        // 检查影院是否存在
        Cinema cinema = getById(cinemaId);
        if (cinema == null) {
            throw new BusinessException("影院不存在");
        }
        
        // 获取影院评价列表
        return cinemaReviewMapper.getReviewsByCinemaId(cinemaId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCinemaReview(Integer cinemaId, CinemaReview review) {
        // 检查影院是否存在
        Cinema cinema = getById(cinemaId);
        if (cinema == null) {
            throw new BusinessException("影院不存在");
        }
        
        // 设置影院ID
        review.setCinemaId(cinemaId);
        
        // 检查评分范围
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new BusinessException("评分范围应为1-5");
        }
        
        // 如果评价时间为空，则设置为当前时间
        if (review.getReviewTime() == null) {
            review.setReviewTime(java.time.LocalDateTime.now());
        }
        
        // 保存评价
        return cinemaReviewMapper.insert(review) > 0;
    }
}
