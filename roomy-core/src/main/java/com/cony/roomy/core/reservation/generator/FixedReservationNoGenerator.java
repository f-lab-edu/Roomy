package com.cony.roomy.core.reservation.generator;

public class FixedReservationNoGenerator implements NoGenerator {
    public static final String FIXED_RESERVATION_NO = "250301X7Y8Z4P2Q5";

    @Override
    public String generate() {
        return FIXED_RESERVATION_NO;
    }
}

