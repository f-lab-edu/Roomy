package com.cony.roomy.core.common.util;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;

@Slf4j
public class TimeUtil {

    public static long getTimeToLive(String timeStr) {
        char unit = findTimeUnit(timeStr);
        long value = findTimeValue(timeStr);

        // unit 기준으로 초시간 변환
        switch (unit) {
            case 'd':
                return Duration.ofDays(value).toMillis();
            case 'h':
                return Duration.ofHours(value).toMillis();
            case 'm':
                return Duration.ofMinutes(value).toMillis();
            case 's':
                return Duration.ofSeconds(value).toMillis();
            default:
                throw new RoomyException(ErrorType.INVALID, Map.of("time property", timeStr), log::warn);

        }
    }

    private static char findTimeUnit(String timeStr) {
        char unit = timeStr.charAt(timeStr.length() - 1);
        if (Character.isDigit(unit)) {
            return 's';
        }
        return unit;
    }

    private static long findTimeValue(String timeStr) {
        if(Character.isDigit(timeStr.charAt(timeStr.length() - 1))) {
            return Long.parseLong(timeStr);
        }
        return Long.parseLong(timeStr.substring(0, timeStr.length() - 1));
    }
}
