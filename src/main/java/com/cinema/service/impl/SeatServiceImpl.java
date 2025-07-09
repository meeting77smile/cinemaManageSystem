package com.cinema.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cinema.common.exception.BusinessException;
import com.cinema.entity.Hall;
import com.cinema.entity.Seat;
import com.cinema.mapper.SeatMapper;
import com.cinema.service.HallService;
import com.cinema.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 座位服务实现类
 */
@Service
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements SeatService {

    @Autowired
    private HallService hallService;

    @Override
    public List<Seat> getSeatsByHallId(Integer hallId) {
        // 验证影厅是否存在
        hallService.getHallById(hallId);
        
        LambdaQueryWrapper<Seat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Seat::getHallId, hallId);
        wrapper.orderByAsc(Seat::getSeatRow).orderByAsc(Seat::getSeatNumber);
        return list(wrapper);
    }

    @Override
    public List<Seat> getAvailableSeatsByShowtime(Integer showtimeId) {
        return baseMapper.getAvailableSeatsByShowtime(showtimeId);
    }

    @Override
    public Seat getSeatById(Integer id) {
        Seat seat = getById(id);
        if (seat == null) {
            throw new BusinessException("座位不存在");
        }
        return seat;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSeat(Seat seat) {
        // 验证影厅是否存在
        hallService.getHallById(seat.getHallId());
        
        // 验证座位是否已存在
        LambdaQueryWrapper<Seat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Seat::getHallId, seat.getHallId())
                .eq(Seat::getSeatRow, seat.getSeatRow())
                .eq(Seat::getSeatNumber, seat.getSeatNumber());
        if (count(wrapper) > 0) {
            throw new BusinessException("座位已存在");
        }
        
        return save(seat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveSeats(Integer hallId, Integer rows, Integer cols) {
        // 验证影厅是否存在
        Hall hall = hallService.getHallById(hallId);
        
        // 清空原有座位
        LambdaQueryWrapper<Seat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Seat::getHallId, hallId);
        remove(wrapper);
        
        // 批量创建座位
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                Seat seat = new Seat();
                seat.setHallId(hallId);
                seat.setSeatRow(i);
                seat.setSeatNumber(j);
                seat.setIsAvailable(true);
                seat.setType("NORMAL");
                seats.add(seat);
            }
        }
        
        boolean result = saveBatch(seats);
        
        // 更新影厅座位容量
        hall.setCapacity(rows * cols);
        hall.setAvailableSeats(rows * cols);
        hallService.updateById(hall);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSeat(Seat seat) {
        Seat existSeat = getById(seat.getSeatId());
        if (existSeat == null) {
            throw new BusinessException("座位不存在");
        }
        
        // 如果修改了所属影厅，需要验证新影厅是否存在
        if (!existSeat.getHallId().equals(seat.getHallId())) {
            hallService.getHallById(seat.getHallId());
            
            // 验证新影厅中是否已存在相同位置的座位
            LambdaQueryWrapper<Seat> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Seat::getHallId, seat.getHallId())
                    .eq(Seat::getSeatRow, seat.getSeatRow())
                    .eq(Seat::getSeatNumber, seat.getSeatNumber());
            if (count(wrapper) > 0) {
                throw new BusinessException("座位已存在");
            }
        }
        
        return updateById(seat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSeat(Integer id) {
        Seat seat = getById(id);
        if (seat == null) {
            throw new BusinessException("座位不存在");
        }
        return removeById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<Integer, Boolean> lockSeats(Integer showtimeId, List<Integer> seatIds) {
        // 获取场次可用座位
        List<Seat> availableSeats = getAvailableSeatsByShowtime(showtimeId);
        
        // 将可用座位ID存入集合，方便查询
        List<Integer> availableSeatIds = new ArrayList<>();
        for (Seat seat : availableSeats) {
            availableSeatIds.add(seat.getSeatId());
        }
        
        // 锁定结果
        Map<Integer, Boolean> lockResult = new HashMap<>();
        
        // 检查每个座位是否可用，并尝试锁定
        for (Integer seatId : seatIds) {
            boolean canLock = availableSeatIds.contains(seatId);
            lockResult.put(seatId, canLock);
            
            // 如果座位可用，则锁定座位（这里只是模拟锁定，实际应该有更复杂的逻辑）
            // 例如，可以在数据库中添加一个临时锁定状态，并设置过期时间
            // 在实际购票时再将状态改为已售出
        }
        
        return lockResult;
    }
}
