package com.cinema.util;

import com.cinema.entity.Seat;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 座位选择工具类
 */
@Component
public class SeatSelectionUtil {

    /**
     * 自动选择座位
     *
     * @param availableSeats 可用座位列表
     * @param count 座位数量
     * @param hallRows 影厅行数
     * @param hallCols 影厅列数
     * @return 选择的座位列表
     */
    public List<Seat> autoSelectSeats(List<Seat> availableSeats, int count, int hallRows, int hallCols) {
        if (availableSeats.size() < count) {
            return Collections.emptyList();
        }

        // 计算影厅中心位置
        int centerRow = hallRows / 2;
        int centerCol = hallCols / 2;

        // 按照到中心点的距离排序座位
        List<Seat> sortedSeats = availableSeats.stream()
                .sorted(Comparator.comparingDouble(seat -> calculateDistance(seat, centerRow, centerCol)))
                .collect(Collectors.toList());

        // 返回最靠近中心的count个座位
        return sortedSeats.subList(0, count);
    }

    /**
     * 计算座位到中心点的距离
     *
     * @param seat 座位
     * @param centerRow 中心行
     * @param centerCol 中心列
     * @return 距离
     */
    private double calculateDistance(Seat seat, int centerRow, int centerCol) {
        int rowDiff = seat.getSeatRow() - centerRow;
        int colDiff = seat.getSeatNumber() - centerCol;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }
}
