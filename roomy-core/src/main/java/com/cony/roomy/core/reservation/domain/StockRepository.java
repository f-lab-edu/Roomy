package com.cony.roomy.core.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByRoomIdAndDateAndUsageType(Long roomId, LocalDate date, UsageType usageType);
}
