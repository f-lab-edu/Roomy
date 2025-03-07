package com.cony.roomy.core.accommodation.service;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.domain.RoomRepository;
import com.cony.roomy.core.accommodation.domain.Stock;
import com.cony.roomy.core.accommodation.domain.StockRepository;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
