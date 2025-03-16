package com.cony.roomy.core.reservation.dto.response;

import com.cony.roomy.core.reservation.domain.Payment;
import com.cony.roomy.core.reservation.domain.PaymentStatus;
import com.cony.roomy.core.reservation.domain.Reservation;
import com.cony.roomy.core.reservation.domain.ReservationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime requestDate;
    private PaymentStatus paymentStatus;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .requestDate(payment.getCreatedDate())
                .paymentStatus(payment.getStatus())
                .build();
    }
}
