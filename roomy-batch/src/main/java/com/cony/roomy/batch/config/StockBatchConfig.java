package com.cony.roomy.batch.config;

import com.cony.roomy.core.accommodation.domain.*;
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
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StockBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    private final int CHUNK_SIZE = 100;
    private final int QUANTITY = 10;

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
                .<Room, List<Stock>> chunk(CHUNK_SIZE, transactionManager)
                .reader(roomItemReader())
                .processor(roomToStockProcessor())
                .writer(stockJpaItemListWriter())
                .faultTolerant()
                .retryLimit(5)
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
                .queryString("select r from Room r")
                .build();
    }

    // 조회한 Room의 Stock을 생성
    @Bean
    @StepScope
    public ItemProcessor<Room, List<Stock>> roomToStockProcessor() {
        return room -> {
            List<Stock> stocks = new ArrayList<>();
            LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
            LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

            // 숙박
            for(LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
                stocks.add(Stock.builder()
                        .room(room)
                        .date(date)
                        .usageType(UsageType.OVERNIGHT)
                        .startTime(LocalTime.of(0,0))
                        .endTime(LocalTime.of(23,59))
                        .quantity(QUANTITY)
                        .build());
            }

            // 대실
            for(LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
                // 대실 가능한 시간대 (12-20) 고정
                LocalTime openTime = LocalTime.of(12,0);
                LocalTime closeTime = LocalTime.of(20,0);

                while(openTime.isBefore(closeTime)) {
                    LocalTime endTime = openTime.plusHours(1);

                    stocks.add(Stock.builder()
                            .room(room)
                            .date(date)
                            .usageType(UsageType.SHORT_STAY)
                            .startTime(openTime)
                            .endTime(endTime)
                            .quantity(QUANTITY)
                            .build());

                    openTime = endTime;
                }
            }


            return stocks;
        };
    }

    // 생성된 Stock 저장
    public JpaItemListWriter<Stock> stockJpaItemListWriter() {
        JpaItemWriter<Stock> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return new JpaItemListWriter<>(jpaItemWriter);
    }
}
