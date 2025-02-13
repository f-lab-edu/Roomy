package com.cony.roomy.core.lib;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BigDecimalVsStringBenchmark {
    public static void main(String[] args) {
        final int N = 10_000_000; // Number of elements to sum
        List<BigDecimal> bigDecimalList = new ArrayList<>(N);
        List<String> numStrList = new ArrayList<>(N);

        // Populate the lists with sample values
        for (int i = 0; i < N; i++) {
            Double dblValue = Math.random();
            BigDecimal bdValue = BigDecimal.valueOf(dblValue);

            bigDecimalList.add(bdValue);
            numStrList.add(String.valueOf(dblValue));
        }

        BigDecimal bigDecimalSum = BigDecimal.ZERO;
        Double doubleSum = 0.0;
        long totalBigDecimalDuration = 0;
        long totalDoubleDuration = 0;
        // Benchmark BigDecimal sum
        for (int i = 0; i < 5; i++) {
            long startTime = System.nanoTime();
            bigDecimalSum = bigDecimalList.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            long endTime = System.nanoTime();
            long bigDecimalDuration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            totalBigDecimalDuration += bigDecimalDuration;

            // Benchmark Double sum
            startTime = System.nanoTime();

            doubleSum = numStrList.stream().mapToDouble(Double::valueOf)
                    .reduce(0.0, Double::sum);

            endTime = System.nanoTime();
            long doubleDuration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            totalDoubleDuration += doubleDuration;
        }

        // Print results
        System.out.println("BigDecimal sum: " + bigDecimalSum + ", Time taken: " + totalBigDecimalDuration / 5 + " ms");
        System.out.println("String sum: " + doubleSum + ", Time taken: " + totalDoubleDuration / 5 + " ms");
    }
}
