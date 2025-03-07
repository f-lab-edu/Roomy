package com.cony.roomy.core.accommodation.dto.response;

import org.springframework.data.domain.Page;

public record GetAccommodationPageResponse(Page<AccommodationResponse> accommodationResponses) {

    public static GetAccommodationPageResponse of(Page<AccommodationResponse> accommodationResponses) {
        return new GetAccommodationPageResponse(accommodationResponses);
    }

}
