package com.cony.roomy.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan(basePackages = "com.cony.roomy.core")
@EnableJpaRepositories(basePackages = "com.cony.roomy.core")
@SpringBootApplication(scanBasePackages = {"com.cony.roomy"})
@EnableFeignClients(basePackages = "com.cony.roomy.core")
public class TestApplication {

}
