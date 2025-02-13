package com.cony.roomy.core.accommodation.dto.response;

import com.cony.roomy.core.accommodation.domain.Accommodation;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationResponse {
    private String name;
    private String type;
    private String city;
    private String address;
    private double rating;
    private int reviewCnt;
    private BigDecimal price;
    private String thumbnailUrl;

    public static AccommodationResponse from(Accommodation accommodation) {
        return AccommodationResponse.builder()
                .name(accommodation.getName())
                .type(accommodation.getType().name())
                .city(accommodation.getAddress().getCity())
                .address(accommodation.getAddress().getFullAddress())
                .rating(accommodation.getRating())
                .price(accommodation.getPrice())
                .build();
    }
}
