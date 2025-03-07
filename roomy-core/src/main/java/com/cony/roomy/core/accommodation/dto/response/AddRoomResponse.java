package com.cony.roomy.core.accommodation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRoomResponse {

    private Long id;
    private String name;
    private int price;
    private int maxGuestCnt;
    private LocalTime checkIn;
    private LocalTime checkOut;
}
