package com.cony.roomy.batch.config;

import com.cony.roomy.batch.dto.ExistingStockDto;
import com.cony.roomy.core.accommodation.domain.*;
import com.cony.roomy.core.reservation.domain.Stock;
import com.cony.roomy.core.reservation.domain.UsageType;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StockBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    private final int CHUNK_SIZE = 1000;
    private final int STOCK_QUANTITY = 10;
    private final int STOCK_DAY = 60; // 현재 날짜 기준 60일치 재고 생성

    @Bean
    public Job createStockJob() {
        return new JobBuilder("createStockJob", jobRepository)
                .preventRestart()
                .start(createStockStep())
                .build();
    }

    @Bean
    public Step createStockStep() {
        return new StepBuilder("createStockStep", jobRepository)
                .<Room, List<Stock>>chunk(CHUNK_SIZE, transactionManager)
                .reader(roomItemReader())
                .processor(roomToStockProcessor())
                .writer(stockJpaItemListWriter())
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .build();

    }

    // Room 조회 Item Reader
    @Bean
    @StepScope
    public JpaPagingItemReader<Room> roomItemReader() {
        return new JpaPagingItemReaderBuilder<Room>()
                .name("roomItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("select r from Room r ORDER BY r.id")
                .saveState(false)
                .build();
    }

    // 조회한 Room의 Stock을 생성
    @Bean
    @StepScope
    public ItemProcessor<Room, List<Stock>> roomToStockProcessor() {
        return room -> {
            LocalDate today = LocalDate.now();
            LocalDate lastDay = today.plusDays(STOCK_DAY);

            // 기존 재고 조회 (JPQL + DTO 활용)
            System.out.println(room.getId());
            Set<ExistingStockDto> existingStocks = getExistingStocks(room.getId(), today, lastDay);

            List<Stock> newStocks = new ArrayList<>();

            for (LocalDate date = today; !date.isAfter(lastDay); date = date.plusDays(1)) {
                // Lambda 내에서 사용하기 위해 final 변수 생성
                final LocalDate currentDate = date;

                boolean hasOvernight = existingStocks.stream()
                        .anyMatch(s -> s.getDate().equals(currentDate) && s.getUsageType() == UsageType.OVERNIGHT);

                if (!hasOvernight) {
                    newStocks.add(Stock.builder()
                            .room(room)
                            .date(currentDate)
                            .usageType(UsageType.OVERNIGHT)
                            .startTime(LocalTime.of(0, 0))
                            .endTime(LocalTime.of(23, 59))
                            .quantity(STOCK_QUANTITY)
                            .build());
                }

                // 12:00~20:00까지 1시간 단위 대실 재고 생성
                for (LocalTime startTime = LocalTime.of(12, 0); startTime.isBefore(LocalTime.of(20, 0)); startTime = startTime.plusHours(1)) {
                    LocalTime endTime = startTime.plusHours(1);

                    // Lambda 내에서 사용하기 위해 final 변수 생성
                    final LocalTime currentStartTime = startTime;

                    boolean hasShortStay = existingStocks.stream()
                            .anyMatch(s -> s.getDate().equals(currentDate) &&
                                    s.getUsageType() == UsageType.SHORT_STAY &&
                                    s.getStartTime().equals(currentStartTime));

                    if (!hasShortStay) {
                        newStocks.add(Stock.builder()
                                .room(room)
                                .date(currentDate)
                                .usageType(UsageType.SHORT_STAY)
                                .startTime(currentStartTime)
                                .endTime(endTime)
                                .quantity(STOCK_QUANTITY)
                                .build());
                    }
                }
            }
            return newStocks;
        };
    }

    // 생성된 Stock jdbc batch insert
//    public JdbcBatchItemListWriter<Stock> stockJdbcBatchItemListWriter() {
//        final String insertQuery =
//                """
//                INSERT INTO stock (room_id, date, usage_type, start_time, end_time, quantity)
//                VALUES (:roomId, :date, :usageType, :startTime, :endTime, :quantity)
//                ON DUPLICATE KEY UPDATE quantity = stock.quantity
//                """;
//
//        JdbcBatchItemWriter<Stock> writer = new JdbcBatchItemWriter<>();
//        writer.setDataSource(dataSource);
//        writer.setSql(insertQuery);
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        writer.afterPropertiesSet();
//
//        return new JdbcBatchItemListWriter<>(writer);
//    }

    // 생성된 Stock 저장
    public JpaItemListWriter<Stock> stockJpaItemListWriter() {
        JpaItemWriter<Stock> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        jpaItemWriter.setClearPersistenceContext(true);

        return new JpaItemListWriter<>(jpaItemWriter);
    }

    // 이미 존재하는 재고 날짜 조회
    private Set<ExistingStockDto> getExistingStocks(Long roomId, LocalDate today, LocalDate lastDay) {
        List<ExistingStockDto> result = entityManagerFactory.createEntityManager()
                .createQuery("""
                                        SELECT new com.cony.roomy.batch.dto.ExistingStockDto(s.date, s.usageType, s.startTime) 
                                        FROM Stock s 
                                        WHERE s.room.id = :roomId 
                                        AND s.date BETWEEN :today AND :lastDay
                        """, ExistingStockDto.class)
                .setParameter("roomId", roomId)
                .setParameter("today", today)
                .setParameter("lastDay", lastDay)
                .getResultList();

        return new HashSet<>(result);
    }
}
