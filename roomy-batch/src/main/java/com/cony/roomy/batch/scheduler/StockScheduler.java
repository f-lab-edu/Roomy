package com.cony.roomy.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StockScheduler {

    private final JobLauncher jobLauncher;
    private final Job createStockJob;

    // 매월 1일 자정(00:00:00) 실행
    @Scheduled(cron = "0 0 0 1 * ?")
    public void runStockBatch() throws Exception {
        JobParameters jobParameters =
                new JobParametersBuilder().addLocalDateTime("now", LocalDateTime.now()).toJobParameters();

        jobLauncher.run(createStockJob, jobParameters);

    }

}
