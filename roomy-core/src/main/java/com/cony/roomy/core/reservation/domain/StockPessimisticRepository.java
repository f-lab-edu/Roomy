package com.cony.roomy.core.reservation.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockPessimisticRepository {

    private final JPAQueryFactory queryFactory;

    public List<Stock> findWithPessimisticLock(Long roomId, LocalDate startDate, LocalDate endDate, UsageType usageType) {
        QStock stock = QStock.stock;

        return queryFactory
                .selectFrom(stock)
                .where(
                        stock.room.id.eq(roomId)
                        .and(stock.date.between(startDate, endDate.minusDays(1)))
                        .and(stock.usageType.eq(usageType))
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();
    }
}
