package com.cony.roomy.core.reservation.service;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.domain.RoomRepository;
import com.cony.roomy.core.reservation.domain.Stock;
import com.cony.roomy.core.reservation.domain.StockRepository;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.reservation.dto.request.StockRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(StockRequest request) throws RoomyException {
        for(LocalDate date = request.getStartDate(); date.isBefore(request.getEndDate()); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            Stock stock = stockRepository.findByRoomIdAndDateAndUsageType(request.getRoomId(), date, request.getUsageType())
                    .orElseThrow(() -> new RoomyException(ErrorType.STOCK_NOT_FOUND, Map.of("roomId", request.getRoomId(), "date", finalDate), log::error));
            if (stock.getQuantity() < 1) {
                throw new RoomyException(ErrorType.OUT_OF_STOCK, Map.of("quantity", stock.getQuantity(), "decrease", 1), log::error);
            }
            stock.decreaseQuantity(1);
            stockRepository.save(stock);

        }
    }

}
