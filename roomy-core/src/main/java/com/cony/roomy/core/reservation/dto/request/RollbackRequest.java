package com.cony.roomy.core.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RollbackRequest {

    private Long paymentId;
    private Long reservationId;

    public static RollbackRequest of(StockRequest stockRequest) {
        return RollbackRequest.builder()
                .paymentId(stockRequest.getPaymentId())
                .reservationId(stockRequest.getReservationId())
                .build();
    }

    public static RollbackRequest of(Long paymentId, Long reservationId) {
        return RollbackRequest.builder()
                .paymentId(paymentId)
                .reservationId(reservationId)
                .build();
    }
}
