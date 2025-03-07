package com.cony.roomy.core.reservation.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class ReservationNoGenerator implements NoGenerator {
    private static final int RANDOM_CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final Random random;

    public ReservationNoGenerator() {
        this.random = new SecureRandom();
    }

    @Override
    public String generate() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomString = generateRandomString(RANDOM_CODE_LENGTH);
        return datePrefix + randomString;
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}