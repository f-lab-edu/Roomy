package com.cony.roomy.core.reservation.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    private Long paymentId;
    private Long reservationId;
    private Long roomId;
    private Long userId;
    private String guestName;
    private String guestPhone;
    private String guestComment;
    private int guestCount;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ReservationRequest of(PaymentRequest paymentRequest) {
        return ReservationRequest.builder()
                .roomId(paymentRequest.getRoomId())
                .userId(paymentRequest.getUserId())
                .guestName(paymentRequest.getGuestName())
                .guestPhone(paymentRequest.getGuestPhone())
                .guestComment(paymentRequest.getGuestComment())
                .guestCount(paymentRequest.getGuestCount())
                .startDate(paymentRequest.getStartDate())
                .endDate(paymentRequest.getEndDate())
                .build();
    }
}