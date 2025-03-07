package com.cony.roomy.core.reservation.service;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.domain.RoomRepository;
import com.cony.roomy.core.reservation.domain.Stock;
import com.cony.roomy.core.reservation.domain.StockRepository;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.reservation.domain.UsageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockService {

    private final RoomRepository roomRepository;
    private final StockRepository stockRepository;

    // 관리자 전용: 수동으로 객실(룸) 재고 추가
    public void createStock(Long roomId, LocalDate date, int quantity) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomyException(ErrorType.ROOM_NOT_FOUND, Map.of("roomId", roomId), log::warn));

        Stock stock = Stock.builder()
                .room(room)
                .date(date)
                .quantity(quantity)
                .build();
        stockRepository.save(stock);
    }

    @Transactional
    public void decrease(Long roomId, LocalDate date, UsageType usageType) {
        Stock stock = stockRepository.findByRoomIdAndDateAndUsageType(roomId, date, usageType)
                .orElseThrow(() -> new RoomyException(ErrorType.STOCK_NOT_FOUND, Map.of("roomId", roomId, "date", date), log::warn));

        stock.decreaseQuantity(1);
        stockRepository.save(stock);
    }

}
