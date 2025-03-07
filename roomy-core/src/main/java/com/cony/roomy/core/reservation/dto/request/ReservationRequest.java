package com.cony.roomy.core.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private Long roomId;
    private Long userId;
    private String guestName;
    private String guestPhone;
    private String guestComment;
    private int guestCount;
    private LocalDate startDate;
    private LocalDate endDate;
}