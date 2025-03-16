package com.cony.roomy.core.reservation.facade;

import com.cony.roomy.core.accommodation.domain.Room;
import com.cony.roomy.core.accommodation.domain.RoomRepository;
import com.cony.roomy.core.reservation.domain.Stock;
import com.cony.roomy.core.reservation.domain.StockRepository;
import com.cony.roomy.core.reservation.domain.UsageType;
import com.cony.roomy.core.reservation.dto.request.StockRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
class StockLockFacadeTest {

    @Autowired
    private StockLockFacade stockLockFacade;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    EntityManager entityManager;

    // 테스트 데이터
    private final Long testRoomId = 3L;
    private final LocalDate startDate = LocalDate.of(2025, 1, 1);
    private final LocalDate endDate = LocalDate.of(2025, 1, 2);
    private final int initialStock = 100;

    public List<Stock> stocks = new ArrayList<>();

    @BeforeEach
    @Transactional
    public void setUp() {
        Room room = roomRepository.findById(testRoomId).get();
        for(LocalDate date=startDate; date.isBefore(endDate);date=date.plusDays(1)) {
            stocks.add(stockRepository.save(
                    Stock.builder()
                            .room(room)
                            .date(date)
                            .usageType(UsageType.OVERNIGHT)
                            .startTime(LocalTime.now())
                            .endTime(LocalTime.now())
                            .quantity(initialStock)
                            .build()
            ));
        }
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll(stocks);
    }

    @Test
    @Transactional
    void 동시_예약_재고_감소_테스트() throws InterruptedException {
        int threadCount = 100; // 동시 요청 수
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        StockRequest stockRequest = StockRequest.builder()
                .roomId(testRoomId)
                .startDate(startDate)
                .endDate(endDate)
                .usageType(UsageType.OVERNIGHT)
                .build();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockLockFacade.decreaseStockWithLock(stockRequest);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        entityManager.flush();
        entityManager.clear();

//        assertThat(stockRepository.findByRoomIdAndDateAndUsageType(testRoomId, startDate, UsageType.OVERNIGHT).get().getQuantity()).isEqualTo(0);
    }
}