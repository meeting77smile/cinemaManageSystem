package com.cinema.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cinema.common.exception.BusinessException;
import com.cinema.entity.Hall;
import com.cinema.entity.Movie;
import com.cinema.entity.Showtime;
import com.cinema.entity.Ticket;
import com.cinema.mapper.ShowtimeMapper;
import com.cinema.service.HallService;
import com.cinema.service.MovieService;
import com.cinema.service.ShowtimeService;
import com.cinema.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 放映场次服务实现类
 */
@Service
public class ShowtimeServiceImpl extends ServiceImpl<ShowtimeMapper, Showtime> implements ShowtimeService {

    @Autowired
    private MovieService movieService;

    @Autowired
    private HallService hallService;

    @Autowired
    @Lazy
    private TicketService ticketService;

    @Override
    public Page<Showtime> pageShowtimes(Page<Showtime> page, Integer movieId, Integer hallId, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Showtime> wrapper = new LambdaQueryWrapper<>();
        if (movieId != null) {
            wrapper.eq(Showtime::getMovieId, movieId);
        }
        if (hallId != null) {
            wrapper.eq(Showtime::getHallId, hallId);
        }
        if (startTime != null) {
            wrapper.ge(Showtime::getStartTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(Showtime::getEndTime, endTime);
        }
        wrapper.orderByAsc(Showtime::getStartTime);
        return page(page, wrapper);
    }

    @Override
    public Showtime getShowtimeById(Integer id) {
        Showtime showtime = getById(id);
        if (showtime == null) {
            throw new BusinessException("放映场次不存在");
        }
        return showtime;
    }

    @Override
    public List<Showtime> getShowtimesByMovieId(Integer movieId) {
        // 验证电影是否存在
        movieService.getMovieById(movieId);
        
        LambdaQueryWrapper<Showtime> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Showtime::getMovieId, movieId);
        wrapper.ge(Showtime::getStartTime, LocalDateTime.now());
        wrapper.orderByAsc(Showtime::getStartTime);
        return list(wrapper);
    }

    @Override
    public List<Showtime> getShowtimesByHallId(Integer hallId, LocalDateTime startTime, LocalDateTime endTime) {
        // 验证影厅是否存在
        hallService.getHallById(hallId);
        
        LambdaQueryWrapper<Showtime> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Showtime::getHallId, hallId);
        if (startTime != null) {
            wrapper.ge(Showtime::getStartTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(Showtime::getEndTime, endTime);
        }
        wrapper.orderByAsc(Showtime::getStartTime);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveShowtime(Showtime showtime) {
        // 验证电影是否存在
        Movie movie = movieService.getMovieById(showtime.getMovieId());
        
        // 验证影厅是否存在
        Hall hall = hallService.getHallById(showtime.getHallId());
        
        // 验证时间段是否冲突
        validateTimeConflict(showtime);
        
        // 设置结束时间
        if (showtime.getEndTime() == null && movie.getDuration() != null) {
            showtime.setEndTime(showtime.getStartTime().plusMinutes(movie.getDuration()));
        }
        
        // 设置可用票数
        if (showtime.getAvailableTickets() == null) {
            showtime.setAvailableTickets(hall.getCapacity());
        }
        
        return save(showtime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateShowtime(Showtime showtime) {
        Showtime existShowtime = getById(showtime.getShowtimeId());
        if (existShowtime == null) {
            throw new BusinessException("放映场次不存在");
        }
        // 如果修改了电影，需要验证新电影是否存在
        if (!existShowtime.getMovieId().equals(showtime.getMovieId())) {
            movieService.getMovieById(showtime.getMovieId());
        }
        // 如果修改了影厅，需要验证新影厅是否存在
        if (!existShowtime.getHallId().equals(showtime.getHallId())) {
            hallService.getHallById(showtime.getHallId());
        }
        // 校验时间段是否冲突
        validateTimeConflict(showtime);
        // 赋值所有字段
        existShowtime.setMovieId(showtime.getMovieId());
        existShowtime.setHallId(showtime.getHallId());
        existShowtime.setStartTime(showtime.getStartTime());
        existShowtime.setEndTime(showtime.getEndTime());
        existShowtime.setTicketPrice(showtime.getTicketPrice());
        existShowtime.setAvailableTickets(showtime.getAvailableTickets());
        existShowtime.setStatus(showtime.getStatus());
        return updateById(existShowtime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeShowtime(Integer id) {
        Showtime showtime = getById(id);
        if (showtime == null) {
            throw new BusinessException("放映场次不存在");
        }
        
        // 删除场次相关的票务信息
        LambdaQueryWrapper<Ticket> ticketWrapper = new LambdaQueryWrapper<>();
        ticketWrapper.eq(Ticket::getShowtimeId, id);
        ticketService.remove(ticketWrapper);
        
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAvailableTickets(Integer showtimeId, Integer count) {
        Showtime showtime = getById(showtimeId);
        if (showtime == null) {
            throw new BusinessException("放映场次不存在");
        }
        
        // 验证票数是否足够
        if (showtime.getAvailableTickets() < count) {
            throw new BusinessException("可用票数不足");
        }
        
        return baseMapper.updateAvailableTickets(showtimeId, count) > 0;
    }

    /**
     * 验证时间段是否冲突
     *
     * @param showtime 放映场次
     */
    private void validateTimeConflict(Showtime showtime) {
        LambdaQueryWrapper<Showtime> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Showtime::getHallId, showtime.getHallId());
        wrapper.ne(showtime.getShowtimeId() != null, Showtime::getShowtimeId, showtime.getShowtimeId());
        wrapper.and(w -> w
                .and(sw -> sw
                        .le(Showtime::getStartTime, showtime.getStartTime())
                        .ge(Showtime::getEndTime, showtime.getStartTime()))
                .or(sw -> sw
                        .le(Showtime::getStartTime, showtime.getEndTime())
                        .ge(Showtime::getEndTime, showtime.getEndTime()))
                .or(sw -> sw
                        .ge(Showtime::getStartTime, showtime.getStartTime())
                        .le(Showtime::getEndTime, showtime.getEndTime())));
        
        if (count(wrapper) > 0) {
            throw new BusinessException("该时间段已有其他场次");
        }
    }
}
