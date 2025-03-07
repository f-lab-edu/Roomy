package com.cony.roomy.batch.dto;

import com.cony.roomy.core.accommodation.domain.UsageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ExistingStockDto {
    private LocalDate date;
    private UsageType usageType;
    private LocalTime startTime;
}
