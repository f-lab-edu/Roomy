package com.cony.roomy.core.reservation.dto.request;

import com.cony.roomy.core.reservation.domain.UsageType;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {

    private Long paymentId;
    private Long reservationId;
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private UsageType usageType;

    public static StockRequest of(ReservationRequest reservationRequest, UsageType usageType) {
        return StockRequest.builder()
                .roomId(reservationRequest.getRoomId())
                .startDate(reservationRequest.getStartDate())
                .endDate(reservationRequest.getEndDate())
                .usageType(usageType)
                .build();
    }


    public void setRollbackIds(Long paymentId, Long reservationId) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
    }
}
