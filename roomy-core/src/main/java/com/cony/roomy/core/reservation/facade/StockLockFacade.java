package com.cony.roomy.core.reservation.facade;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.reservation.domain.Stock;
import com.cony.roomy.core.reservation.domain.StockRepository;
import com.cony.roomy.core.reservation.domain.UsageType;
import com.cony.roomy.core.reservation.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockLockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public void decreaseStockWithLock(Long roomId, LocalDate date, UsageType usageType) {
        String lockKey = "stock:room:" + roomId + ":date:" + date;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 획득 시도 시간, 점유 시간
            lock.tryLock(10, 1, TimeUnit.SECONDS);

            stockService.decrease(roomId, date, usageType);
        } catch (InterruptedException e) {
            throw new RoomyException(ErrorType.REDISSON_LOCK_FAIL, Map.of("error", e.getMessage()), log::error);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
