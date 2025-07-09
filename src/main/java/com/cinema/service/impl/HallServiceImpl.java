package com.cinema.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cinema.common.exception.BusinessException;
import com.cinema.entity.Hall;
import com.cinema.entity.Seat;
import com.cinema.entity.Showtime;
import com.cinema.mapper.HallMapper;
import com.cinema.service.CinemaService;
import com.cinema.service.HallService;
import com.cinema.service.SeatService;
import com.cinema.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 影厅服务实现类
 */
@Service
public class HallServiceImpl extends ServiceImpl<HallMapper, Hall> implements HallService {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    @Lazy
    private SeatService seatService;

    @Autowired
    @Lazy
    private ShowtimeService showtimeService;

    @Override
    public Page<Hall> pageHalls(Page<Hall> page, Integer cinemaId, String name) {
        LambdaQueryWrapper<Hall> wrapper = new LambdaQueryWrapper<>();
        if (cinemaId != null) {
            wrapper.eq(Hall::getCinemaId, cinemaId);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(Hall::getName, name);
        }
        wrapper.orderByDesc(Hall::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Hall getHallById(Integer id) {
        Hall hall = getById(id);
        if (hall == null) {
            throw new BusinessException("影厅不存在");
        }
        return hall;
    }

    @Override
    public List<Hall> getHallsByCinemaId(Integer cinemaId) {
        // 验证影院是否存在
        cinemaService.getCinemaById(cinemaId);
        
        LambdaQueryWrapper<Hall> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hall::getCinemaId, cinemaId);
        wrapper.orderByAsc(Hall::getName);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHall(Hall hall) {
        // 验证影院是否存在
        cinemaService.getCinemaById(hall.getCinemaId());
        // 新增：自动设置可用座位数
        hall.setAvailableSeats(hall.getCapacity());
        return save(hall);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHall(Hall hall) {
        Hall existHall = getById(hall.getHallId());
        if (existHall == null) {
            throw new BusinessException("影厅不存在");
        }
        
        // 如果修改了所属影院，需要验证新影院是否存在
        if (!existHall.getCinemaId().equals(hall.getCinemaId())) {
            cinemaService.getCinemaById(hall.getCinemaId());
        }
        
        return updateById(hall);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeHall(Integer id) {
        Hall hall = getById(id);
        if (hall == null) {
            throw new BusinessException("影厅不存在");
        }
        
        // 删除影厅相关的座位
        LambdaQueryWrapper<Seat> seatWrapper = new LambdaQueryWrapper<>();
        seatWrapper.eq(Seat::getHallId, id);
        seatService.remove(seatWrapper);
        
        // 删除影厅相关的场次
        LambdaQueryWrapper<Showtime> showtimeWrapper = new LambdaQueryWrapper<>();
        showtimeWrapper.eq(Showtime::getHallId, id);
        showtimeService.remove(showtimeWrapper);
        
        // 删除影厅本身
        return removeById(id);
    }
}
