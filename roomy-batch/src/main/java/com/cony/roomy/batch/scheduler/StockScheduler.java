package com.cony.roomy.batch.scheduler;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockScheduler {

    private final JobLauncher jobLauncher;
    private final Job createStockJob;

    // 매일 00:00 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void runStockBatch()  {
        LocalDateTime now = LocalDateTime.now();
        try {
            JobParameters jobParameters = new JobParametersBuilder().addLocalDateTime("now", now).toJobParameters();

            jobLauncher.run(createStockJob, jobParameters);

            log.info("Stock Batch 실행 완료: {}", now);
        } catch (Exception e) {
            throw new RoomyException(ErrorType.BATCH_FAIL, Map.of("Batch Exception", e.getMessage(), "Time", now), log::error);
        }
    }

}
