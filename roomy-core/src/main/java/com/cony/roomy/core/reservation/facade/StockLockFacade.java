package com.cony.roomy.core.reservation.facade;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.reservation.domain.Stock;
import com.cony.roomy.core.reservation.domain.StockRepository;
import com.cony.roomy.core.reservation.domain.UsageType;
import com.cony.roomy.core.reservation.dto.request.RollbackRequest;
import com.cony.roomy.core.reservation.dto.request.StockRequest;
import com.cony.roomy.core.reservation.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockLockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

//    @Transactional
    @KafkaListener(topics = "STOCK_DECREASE_EVENT", groupId = "stock-group", containerFactory = "kafkaListenerContainerFactory")
    public void decreaseStockWithLock(StockRequest stockRequest) {
        String lockKey = "stock:room:" + stockRequest.getRoomId();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if(lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                stockService.decrease(stockRequest);
            }
        } catch (InterruptedException e) {
            throw new RoomyException(ErrorType.REDISSON_LOCK_FAIL, Map.of("error", e.getMessage()), log::error);
        } catch (RoomyException e) {
            // 재고 이슈로 예외 발생 시 예약 취소 및 결제 취소 이벤트(롤백 이벤트) 발행
            kafkaTemplate.send("ROLLBACK_EVENT", RollbackRequest.of(stockRequest));
            log.error(e.getMessage());

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
