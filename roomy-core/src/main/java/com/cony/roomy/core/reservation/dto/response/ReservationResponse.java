package com.cony.roomy.core.reservation.dto.response;

import com.cony.roomy.core.reservation.domain.Reservation;
import com.cony.roomy.core.reservation.domain.ReservationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private String reservationNo;
    private String guestName;
    private String guestPhone;
    private String guestComment;
    private int guestCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus status;

    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationNo(reservation.getReservationNo())
                .guestName(reservation.getGuestName())
                .guestPhone(reservation.getGuestPhone())
                .guestComment(reservation.getGuestComment())
                .guestCount(reservation.getGuestCount())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .status(reservation.getStatus())
                .build();
    }
}
